package com.simullim.service

internal interface PlayServiceStatus {
    data object Playing : PlayServiceStatus
    data object Paused : PlayServiceStatus
    data object Initialized : PlayServiceStatus
    data object Ready : PlayServiceStatus
    data class Error(val exceptions: List<Exception>) : PlayServiceStatus
}