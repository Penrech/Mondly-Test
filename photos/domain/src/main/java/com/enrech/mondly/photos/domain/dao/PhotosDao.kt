package com.enrech.mondly.photos.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

    @Query("delete from PhotoEntity")
    suspend fun clearAllPhotos()

    @Query("select * from PhotoEntity where id = :id")
    suspend fun getPhoto(id: Long): PhotoEntity?

    @Query("select * from PhotoEntity order by id")
    suspend fun getAllPhotos(): List<PhotoEntity>

    @Query("select * from PhotoEntity order by id")
    fun getAllPhotosFlow(): Flow<List<PhotoEntity>>
}