package com.example.gps_tracker

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import com.simullim.millsToSec
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GpsTracker(private val locationManager: LocationManager) {
    private val _gpsDataStateFlow = MutableStateFlow(GpsDataModel())
    val gpsDataStateFlow = _gpsDataStateFlow.asStateFlow()
    var lastLocation: Location? = null
        private set
    private val locationListener = LocationListener(::updateWithEmitLocation)
    private var getCurrentLocationJob: Job? = null

    @RequiresPermission(ACCESS_FINE_LOCATION)
    fun start() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000,
            10f,
            locationListener
        )
    }

    fun stop() {
        locationManager.removeUpdates(locationListener)
    }

    private fun updateWithEmitLocation(location: Location) {
        _gpsDataStateFlow.update {
            val distance =
                lastLocation?.let { lastLocation -> location.distanceTo(lastLocation) } ?: 0f
            val currentSpeed = location.speed
            val totalDistance = it.totalDistanceMeter + distance
            val totalTime =
                it.totalTimeMills + (lastLocation?.let { lastLocation -> location.time - lastLocation.time }
                    ?: 0)
            val averageVelocity = totalDistance / millsToSec(totalTime)
            GpsDataModel(
                currentVelocity = currentSpeed,
                averageVelocity = averageVelocity,
                totalDistanceMeter = totalDistance,
                totalTimeMills = totalTime
            )
        }

        lastLocation = location
    }
}