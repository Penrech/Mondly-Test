package com.enrech.mondly.core.data.repository

import com.enrech.mondly.core.data.util.InternetNetworkCallback
import com.enrech.mondly.core.domain.repository.InternetStateRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class InternetStateRepositoryImpl @Inject constructor(
    private val internetNetworkCallback: InternetNetworkCallback
): InternetStateRepository {
    override fun getStateFlow(): StateFlow<Boolean?> = internetNetworkCallback.internetConnectivityState
}