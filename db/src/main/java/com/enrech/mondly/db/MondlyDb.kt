package com.enrech.mondly.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.enrech.mondly.photos.domain.dao.PhotosDao
import com.enrech.mondly.photos.domain.entity.PhotoEntity

@Database(
    entities = [PhotoEntity::class],
    version = 1
)
abstract class MondlyDb: RoomDatabase() {
    abstract fun getPhotosDao(): PhotosDao
}