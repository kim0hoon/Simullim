package com.simullim

fun meterToKiloMeterMeterString(meter: Long): String {
    val kiloMeter = meter / 1000
    return String.format(null, "%dkm %dm", kiloMeter, meter % 1000)
}