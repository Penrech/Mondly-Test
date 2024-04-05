package com.enrech.mondly.photos.data.repository

import com.enrech.mondly.photos.domain.dao.PhotosDao
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.domain.repository.PhotosDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotosDbRepositoryImpl @Inject constructor(
    private val dao: PhotosDao
): PhotosDbRepository {

    override suspend fun addPhotos(photos: List<PhotoEntity>) = withContext(Dispatchers.IO) {
        dao.insertPhotos(photos)
    }

    override suspend fun getPhoto(id: Long): PhotoEntity? = withContext(Dispatchers.IO) {
        dao.getPhoto(id)
    }

    override suspend fun clearAllPhotos() = withContext(Dispatchers.IO) {
        dao.clearAllPhotos()
    }

    override suspend fun getAllPhotos(): List<PhotoEntity> = withContext(Dispatchers.IO) {
        dao.getAllPhotos()
    }

    override fun getAllPhotosFlow(): Flow<List<PhotoEntity>> =
        dao.getAllPhotosFlow().flowOn(Dispatchers.IO)
}