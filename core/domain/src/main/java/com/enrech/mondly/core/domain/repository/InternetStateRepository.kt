package com.enrech.mondly.core.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface InternetStateRepository {
    fun getStateFlow(): StateFlow<Boolean?>
}