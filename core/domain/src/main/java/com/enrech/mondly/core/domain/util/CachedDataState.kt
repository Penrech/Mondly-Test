package com.enrech.mondly.core.domain.util

data class CachedDataState <T>(
    val data: T,
    val secondaryError: Throwable? = null
)
