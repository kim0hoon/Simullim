package com.musicplayer

sealed interface Status {
    data object Initialized : Status
    data object Ready : Status
    data object Playing : Status
    data object Paused : Status
    data object Released : Status
    data class Error(val exception: Exception) : Status
}