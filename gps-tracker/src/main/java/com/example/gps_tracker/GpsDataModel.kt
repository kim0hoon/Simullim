package com.example.gps_tracker

data class GpsDataModel(
    val currentVelocity: Double = 0.0,
    val averageVelocity: Double = 0.0,
    val totalDistanceMeter: Double = 0.0
)
