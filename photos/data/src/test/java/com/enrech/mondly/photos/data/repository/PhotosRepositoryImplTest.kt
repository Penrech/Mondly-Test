package com.enrech.mondly.photos.data.repository

import com.enrech.mondly.core.domain.exception.NoInternetException
import com.enrech.mondly.core.domain.repository.InternetStateRepository
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.domain.repository.PhotosDbRepository
import com.enrech.mondly.photos.domain.repository.PhotosRemoteRepository
import com.enrech.mondly.photos.domain.repository.PhotosRepository
import com.google.common.truth.Truth
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PhotosRepositoryImplTest {

    @MockK
    private lateinit var dbRepository: PhotosDbRepository

    @MockK
    private lateinit var remoteRepository: PhotosRemoteRepository

    @MockK
    private lateinit var internetStateRepository: InternetStateRepository

    private lateinit var repo: PhotosRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = PhotosRepositoryImpl(dbRepository, remoteRepository, internetStateRepository)
    }

    @Test
    fun `given getAllPhotos fail in loadUpdatePhotos and no cached data, return failure`() = runTest {
        val failure = NoInternetException()
        val response = Result.failure<List<PhotoEntity>>(failure)

        coEvery { remoteRepository.getAllPhotos() } returns response
        coEvery { dbRepository.getAllPhotos() } returns listOf()

        val result = repo.loadUpdatePhotos()

        Truth.assertThat(result.exceptionOrNull() == failure)
    }

    @Test
    fun `given getAllPhotos fail in loadUpdatePhotos and cached data, return cached data with secondary failure`() = runTest {
        val failure = NoInternetException()
        val response = Result.failure<List<PhotoEntity>>(failure)
        val dataItem = mockk<PhotoEntity>()

        coEvery { remoteRepository.getAllPhotos() } returns response
        coEvery { dbRepository.getAllPhotos() } returns listOf(dataItem)

        val result = repo.loadUpdatePhotos()

        assertEquals(result.getOrNull()?.data?.firstOrNull(), dataItem)
        assertEquals(result.getOrNull()?.secondaryError, failure)
    }

    @Test
    fun `given getAllPhotos succeed, cache data and return success with cached data and no secondary failure`() = runTest {
        val responseDataItem = mockk<PhotoEntity>()
        val slot = CapturingSlot<List<PhotoEntity>>()

        coEvery { remoteRepository.getAllPhotos() } returns Result.success(listOf(responseDataItem))
        coEvery { dbRepository.updatePhotos(any()) } returns Unit

        val result = repo.loadUpdatePhotos()

        coVerify {
            dbRepository.updatePhotos(capture(slot))
        }

        assertEquals(result.getOrNull()?.data?.firstOrNull(), responseDataItem)
        assertEquals(slot.captured.firstOrNull(), responseDataItem)
    }

    @Test
    fun `given getPhotosFlow with no internet and no cached data, return no internet failure`() = runTest {
        every { internetStateRepository.getStateFlow() } returns MutableStateFlow(false)
        every { dbRepository.getAllPhotosFlow() } returns flowOf(listOf())

        val job = repo.getPhotosFlow().onEach {
            Truth.assertThat(it.exceptionOrNull() is NoInternetException)
        }.launchIn(this)

        advanceUntilIdle()
        job.cancel()
    }

    @Test
    fun `given getPhotosFlow with no internet and cached data, return cached data`() = runTest {
        val cachedItem = mockk<PhotoEntity>()

        coEvery { internetStateRepository.getStateFlow() } returns MutableStateFlow(false)
        coEvery { dbRepository.getAllPhotosFlow() } returns flowOf(listOf(cachedItem))

        val job = repo.getPhotosFlow().onEach {
            assertEquals(it.getOrNull()?.firstOrNull(), cachedItem)
        }.launchIn(this)

        advanceUntilIdle()
        job.cancel()
    }

    @Test
    fun `given getPhotosFlow with internet and no cached data, return empty cached data`() = runTest {
        coEvery { internetStateRepository.getStateFlow() } returns MutableStateFlow(true)
        coEvery { dbRepository.getAllPhotosFlow() } returns flowOf(listOf())

        val job = repo.getPhotosFlow().onEach {
            Truth.assertThat(it.getOrNull()?.isEmpty())
        }.launchIn(this)

        advanceUntilIdle()
        job.cancel()
    }
}