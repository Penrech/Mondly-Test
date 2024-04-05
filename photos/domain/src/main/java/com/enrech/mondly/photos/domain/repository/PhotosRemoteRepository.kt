package com.enrech.mondly.photos.domain.repository

import com.enrech.mondly.photos.domain.entity.PhotoEntity

interface PhotosRemoteRepository {
    suspend fun getAllPhotos(): Result<List<PhotoEntity>>
}