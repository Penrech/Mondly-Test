package com.enrech.mondly.photos.data.repository

import com.enrech.mondly.core.domain.exception.NoInternetException
import com.enrech.mondly.core.domain.repository.InternetStateRepository
import com.enrech.mondly.photos.data.mapper.PhotoResponseMapper
import com.enrech.mondly.photos.data.service.PhotoService
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.domain.repository.PhotosRemoteRepository
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRemoteRepositoryImpl @Inject constructor(
    private val service: PhotoService,
    private val mapper: PhotoResponseMapper,
    private val internetStateRepository: InternetStateRepository
): PhotosRemoteRepository {
    override suspend fun getAllPhotos(): Result<List<PhotoEntity>> = runCatching {
        mapper.mapFrom(service.getContent())
    }.fold(
        onSuccess = { Result.success(it) },
        onFailure = {
            val isInternetConnected = internetStateRepository.getStateFlow().value == true
            val result = when {
                isInternetConnected || it is UnknownHostException -> NoInternetException()
                else -> it
            }
            Result.failure(result)
        }
    )
}