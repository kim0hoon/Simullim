package com.simullim

import java.util.concurrent.TimeUnit

fun millsToHourMinSecString(mills: Long): String {
    val hour = millsToHour(mills)
    val min = millsToMin(mills) % 60
    val sec = millsToSec(mills) % 60
    return String.format(null, "%d:%02d:%02d", hour, min, sec)
}

fun millsToMinSecString(mills: Long): String {
    val min = millsToMin(mills)
    val sec = millsToSec(mills)
    return String.format(null, "%d:%02d", min, sec)
}

fun millsToSec(mills: Long) = TimeUnit.MILLISECONDS.toSeconds(mills)
fun millsToMin(mills: Long) = TimeUnit.MILLISECONDS.toMinutes(mills)
fun millsToHour(mills: Long) = TimeUnit.MILLISECONDS.toHours(mills)