package com.example.gps_tracker

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GpsTracker(private val locationManager: LocationManager) {
    private val _gpsDataStateFlow = MutableStateFlow(GpsDataModel())
    val gpsDataStateFlow = _gpsDataStateFlow.asStateFlow()
    private val locationListener =
        LocationListener { location ->
            location.run {
                Log.d("TESTLOG", "accuracy : ${speedAccuracyMetersPerSecond} speed : $speed")

            }
        }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    fun start() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            10f,
            locationListener
        )
    }

    fun release() {
        locationManager.removeUpdates(locationListener)
    }
}