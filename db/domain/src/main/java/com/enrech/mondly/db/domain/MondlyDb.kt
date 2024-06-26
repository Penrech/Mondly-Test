package com.enrech.mondly.db.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enrech.mondly.photos.domain.dao.PhotosDao
import com.enrech.mondly.photos.domain.entity.PhotoEntity

@Database(
    entities = [PhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MondlyDb: RoomDatabase() {
    abstract fun getPhotosDao(): PhotosDao
}