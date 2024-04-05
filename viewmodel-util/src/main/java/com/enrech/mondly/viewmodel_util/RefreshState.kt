package com.enrech.mondly.viewmodel_util

data class RefreshState(
    var forceRefresh: Boolean = true,
    var isAutoRefreshing: Boolean = false
)