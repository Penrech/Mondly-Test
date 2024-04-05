package com.enrech.mondly.photos.data.di

import com.enrech.mondly.db.domain.repository.DbRepository
import com.enrech.mondly.photos.data.repository.PhotosDbRepositoryImpl
import com.enrech.mondly.photos.data.repository.PhotosRemoteRepositoryImpl
import com.enrech.mondly.photos.data.repository.PhotosRepositoryImpl
import com.enrech.mondly.photos.domain.dao.PhotosDao
import com.enrech.mondly.photos.domain.repository.PhotosDbRepository
import com.enrech.mondly.photos.domain.repository.PhotosRemoteRepository
import com.enrech.mondly.photos.domain.repository.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotosModule {

    @Binds
    @Singleton
    abstract fun bindPhotosRepository(impl: PhotosRepositoryImpl): PhotosRepository

    @Binds
    @Singleton
    abstract fun bindPhotosRemoteRepository(impl: PhotosRemoteRepositoryImpl): PhotosRemoteRepository

    @Binds
    @Singleton
    abstract fun bindsPhotosDbRepository(impl: PhotosDbRepositoryImpl): PhotosDbRepository

    companion object {
        @Provides
        @Singleton
        fun providePhotosDao(dbRepository: DbRepository): PhotosDao = dbRepository.getDb().getPhotosDao()
    }
}