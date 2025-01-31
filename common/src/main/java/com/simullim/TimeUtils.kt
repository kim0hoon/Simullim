package com.simullim

import java.util.concurrent.TimeUnit

fun millsToHourMinSecString(mills: Long): String {
    val hour = TimeUnit.MILLISECONDS.toHours(mills)
    val min = TimeUnit.MILLISECONDS.toMinutes(mills) % 60
    val sec = TimeUnit.MILLISECONDS.toSeconds(mills) % 60
    return String.format(null, "%d:%02d:%02d", hour, min, sec)
}

fun millsToMinSecString(mills: Long): String {
    val min = TimeUnit.MILLISECONDS.toMinutes(mills)
    val sec = TimeUnit.MILLISECONDS.toSeconds(mills) % 60
    return String.format(null, "%d:%02d", min, sec)
}