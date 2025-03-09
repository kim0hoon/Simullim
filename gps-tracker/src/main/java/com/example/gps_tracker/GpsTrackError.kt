package com.example.gps_tracker

import com.google.android.gms.common.api.ResolvableApiException

sealed interface GpsTrackError {
    val exception: Exception

    data class Location(override val exception: ResolvableApiException) : GpsTrackError
    data class Unknown(override val exception: Exception) : GpsTrackError
}