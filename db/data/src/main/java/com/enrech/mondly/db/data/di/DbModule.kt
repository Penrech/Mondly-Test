package com.enrech.mondly.db.data.di

import android.content.Context
import androidx.room.Room
import com.enrech.mondly.db.data.Constant
import com.enrech.mondly.db.data.repository.DbRepositoryImpl
import com.enrech.mondly.db.domain.MondlyDb
import com.enrech.mondly.db.domain.repository.DbRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DbModule {

    @Binds
    @Singleton
    abstract fun bindsDbRepository(impl: DbRepositoryImpl): DbRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): MondlyDb =
            Room.databaseBuilder(
                context,
                MondlyDb::class.java,
                Constant.dbName
            ).build()
    }
}