package com.musicplayer

data class TrackInfo(
    val currentName: String?,
    val currentDurationMills: Long,
    val currentTotalDurationMills: Long,
    val currentTrackIndex: Int
)
