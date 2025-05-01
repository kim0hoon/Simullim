package com.simullim

fun meterPerSecToSecPerKiloMeter(velocity: Float): Float {
    return if (velocity == 0f || velocity.isNaN()) 0f
    else 1000f / velocity
}
