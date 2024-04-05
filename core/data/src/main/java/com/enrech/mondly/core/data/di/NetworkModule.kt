package com.enrech.mondly.core.data.di

import android.content.Context
import android.net.ConnectivityManager
import com.enrech.mondly.core.data.repository.InternetStateRepositoryImpl
import com.enrech.mondly.core.data.util.InternetNetworkCallback
import com.enrech.mondly.core.domain.repository.InternetStateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindInternetStateRepository(impl: InternetStateRepositoryImpl): InternetStateRepository

    companion object {
        @Provides
        @Singleton
        fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @Provides
        @Singleton
        fun provideInternetConnectivityMonitor(
            connectivityManager: ConnectivityManager
        ): InternetNetworkCallback = InternetNetworkCallback(connectivityManager)

    }
}