package com.enrech.mondly.photos.domain.repository

import com.enrech.mondly.photos.domain.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotosDbRepository {
    suspend fun addPhotos(photos: List<PhotoEntity>)

    suspend fun getPhoto(id: Long): PhotoEntity?

    suspend fun clearAllPhotos()

    suspend fun getAllPhotos(): List<PhotoEntity>

    fun getAllPhotosFlow(): Flow<List<PhotoEntity>>
}