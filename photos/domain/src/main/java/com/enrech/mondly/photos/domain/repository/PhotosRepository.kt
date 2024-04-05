package com.enrech.mondly.photos.domain.repository

import com.enrech.mondly.photos.domain.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    suspend fun loadUpdatePhotos(): Result<Unit>

    fun getPhotosFlow(): Flow<Result<List<PhotoEntity>>>
}