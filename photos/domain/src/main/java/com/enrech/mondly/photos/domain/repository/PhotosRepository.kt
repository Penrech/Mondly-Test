package com.enrech.mondly.photos.domain.repository

import com.enrech.mondly.core.domain.util.CachedDataState
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    suspend fun loadUpdatePhotos(): Result<CachedDataState<List<PhotoEntity>>>

    fun getPhotosFlow(): Flow<Result<List<PhotoEntity>>>
}