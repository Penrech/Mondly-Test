package com.enrech.mondly.photos.data.repository

import com.enrech.mondly.core.domain.exception.NoInternetException
import com.enrech.mondly.core.domain.repository.InternetStateRepository
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.domain.repository.PhotosDbRepository
import com.enrech.mondly.photos.domain.repository.PhotosRemoteRepository
import com.enrech.mondly.photos.domain.repository.PhotosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PhotosRepositoryImpl @Inject constructor(
    private val dbRepository: PhotosDbRepository,
    private val remoteRepository: PhotosRemoteRepository,
    private val internetStateRepository: InternetStateRepository
): PhotosRepository {
    override suspend fun loadUpdatePhotos(): Result<Unit> =
        remoteRepository.getAllPhotos()
            .map {
                dbRepository.updatePhotos(it)
            }

    override fun getPhotosFlow(): Flow<Result<List<PhotoEntity>>> =
        combine(
            internetStateRepository.getStateFlow(),
            dbRepository.getAllPhotosFlow()
        ) { internetConnected, photos ->
            when {
                internetConnected == false && photos.isEmpty() -> Result.success(photos)
                else -> Result.failure(NoInternetException())
            }
        }
}