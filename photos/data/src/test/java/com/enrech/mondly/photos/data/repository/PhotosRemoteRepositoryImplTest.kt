package com.enrech.mondly.photos.data.repository

import com.enrech.mondly.core.domain.exception.NoInternetException
import com.enrech.mondly.core.domain.repository.InternetStateRepository
import com.enrech.mondly.photos.data.mapper.PhotoResponseMapper
import com.enrech.mondly.photos.data.service.PhotoService
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.domain.repository.PhotosRemoteRepository
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException


class PhotosRemoteRepositoryImplTest {

    @MockK
    private lateinit var service: PhotoService

    @MockK
    private lateinit var mapper: PhotoResponseMapper

    @MockK
    private lateinit var internetStateRepository: InternetStateRepository

    private lateinit var repo: PhotosRemoteRepository

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        repo = PhotosRemoteRepositoryImpl(service, mapper, internetStateRepository)
    }

    @Test
    fun `given service crash with unknown host exception, expect failure with no internet exception`() = runTest {
        val exception = UnknownHostException()

        coEvery { mapper.mapFrom(any()) } returns listOf()
        coEvery { service.getContent() } throws exception
        coEvery { internetStateRepository.getStateFlow().value } returns true

        val result = repo.getAllPhotos()

        Truth.assertThat(result.exceptionOrNull() == exception)
    }

    @Test
    fun `given service crash with some exception and there is no internet, expect failure with no internet exception`() = runTest {
        val exception = IllegalStateException()

        coEvery { mapper.mapFrom(any()) } returns listOf()
        coEvery { service.getContent() } throws exception
        coEvery { internetStateRepository.getStateFlow().value } returns false

        val result = repo.getAllPhotos()

        Truth.assertThat(result.exceptionOrNull() is NoInternetException)
    }

    @Test
    fun `given service crash with x exception and there's internet, expect failure with x exception`() = runTest {
        val exception = IllegalStateException()

        coEvery { mapper.mapFrom(any()) } returns listOf()
        coEvery { service.getContent() } throws exception
        coEvery { internetStateRepository.getStateFlow().value } returns true

        val result = repo.getAllPhotos()

        Truth.assertThat(result.exceptionOrNull() == exception)
    }

    @Test
    fun `given service operation succeed, expect result success with service data mapped`() = runTest {
        val expected = mockk<PhotoEntity>()

        coEvery { mapper.mapFrom(any()) } returns listOf(expected)
        coEvery { service.getContent() } returns mockk()

        val result = repo.getAllPhotos()

        coVerify(exactly = 0) {
            internetStateRepository.getStateFlow()
        }

        Truth.assertThat(result.getOrNull()?.firstOrNull() == expected)
    }
}