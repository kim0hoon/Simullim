package com.example.gps_tracker

sealed interface Status {
    data object Tracking : Status
    data object Paused : Status
    data class Error(val exception: Exception) : Status
}