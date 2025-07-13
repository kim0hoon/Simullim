package com.example.gps_tracker

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.simullim.DATA_EMIT_DELAY_MILLS
import com.simullim.millsToSec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class GpsTracker(private val context: Context) {
    private val fusedLocationClient get() = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, DATA_EMIT_DELAY_MILLS)
            .setMinUpdateIntervalMillis(DATA_EMIT_DELAY_MILLS)
            .build()
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                updateWithEmitLocation(it)
            }
        }
    }
    private val oneShotLocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1).setMaxUpdates(1)
            .setMinUpdateIntervalMillis(1).build()
    private val oneShotLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                updateWithEmitLocation(it)
            }
        }
    }

    private val _gpsDataStateFlow = MutableStateFlow(GpsData())
    val gpsDataStateFlow = _gpsDataStateFlow.asStateFlow()

    var lastLocation: Location? = null
        private set

    private val _statusStateFlow = MutableStateFlow<Status>(Status.Paused)
    val statusStateFlow = _statusStateFlow.asStateFlow()

    @RequiresPermission(ACCESS_FINE_LOCATION)
    fun start() {
        if (statusStateFlow.value is Status.Tracking) return
        checkLocationSetting(onSuccess = {
            startTracking()
        }, onFailure = ::onFailureCheckLocationSetting)
    }

    private fun checkLocationSetting(
        onSuccess: (LocationSettingsResponse) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val builder = LocationSettingsRequest.Builder()
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        client.checkLocationSettings(builder.build()).run {
            addOnSuccessListener(onSuccess)
            addOnFailureListener(onFailure)
        }
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    private fun startTracking() {
        lastLocation = null
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        _statusStateFlow.value = Status.Tracking
    }

    private fun onFailureCheckLocationSetting(exception: Exception) {
        exception.message?.let {
            Timber.e(it)
        }
        _statusStateFlow.value = Status.Error(exception)
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    fun pause() {
        executeOneShotLocationRequest()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        _statusStateFlow.value = Status.Paused
    }

    fun pauseNotUpdated() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        _statusStateFlow.value = Status.Paused
    }

    fun stop() {
        _gpsDataStateFlow.value = GpsData()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        _statusStateFlow.value = Status.Paused
    }

    fun release() {
        fusedLocationClient.run {
            removeLocationUpdates(locationCallback)
            removeLocationUpdates(oneShotLocationCallback)
        }
    }

    @RequiresPermission(ACCESS_FINE_LOCATION)
    private fun executeOneShotLocationRequest() {
        fusedLocationClient.requestLocationUpdates(
            oneShotLocationRequest,
            oneShotLocationCallback,
            Looper.getMainLooper()
        )
        _statusStateFlow.value = Status.Tracking
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
            GpsData(
                currentVelocity = currentSpeed,
                averageVelocity = averageVelocity,
                totalDistanceMeter = totalDistance,
                totalTimeMills = totalTime
            )
        }

        lastLocation = location
    }
}