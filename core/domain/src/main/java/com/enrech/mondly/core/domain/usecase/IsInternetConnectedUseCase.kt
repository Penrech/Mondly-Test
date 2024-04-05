package com.enrech.mondly.core.domain.usecase

import com.enrech.mondly.core.domain.repository.InternetStateRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsInternetConnectedUseCase @Inject constructor(
    private val internetStateRepository: InternetStateRepository
) {
    fun isConnected(): StateFlow<Boolean?> = internetStateRepository.getStateFlow()
}