package com.enrech.mondly.db.di

import android.content.Context
import androidx.room.Room
import com.enrech.mondly.db.Constant
import com.enrech.mondly.db.MondlyDb
import com.enrech.mondly.photos.domain.dao.PhotosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DbModule {

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): MondlyDb =
            Room.databaseBuilder(
                context,
                MondlyDb::class.java,
                Constant.dbName
            ).build()

        @Provides
        @Singleton
        fun providePhotosDao(db: MondlyDb): PhotosDao = db.getPhotosDao()
    }
}