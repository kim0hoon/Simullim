package com.example.gps_tracker

/**
 * @param currentVelocity : meter per sec
 * @param averageVelocity : meter per sec
 * @param totalDistanceMeter : meter
 * @param totalTimeMills : millisecond
 */
data class GpsDataModel(
    val currentVelocity: Float = 0f,
    val averageVelocity: Float = 0f,
    val totalDistanceMeter: Float = 0f,
    val totalTimeMills: Long = 0L
)
