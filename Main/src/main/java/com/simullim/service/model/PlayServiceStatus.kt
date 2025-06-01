package com.simullim.service.model

internal interface PlayServiceStatus {
    data object Playing : PlayServiceStatus
    data object Paused : PlayServiceStatus
    data object Initialized : PlayServiceStatus
    data object Ready : PlayServiceStatus
    data object Released : PlayServiceStatus
    data class Error(val exception: Exception) : PlayServiceStatus
}