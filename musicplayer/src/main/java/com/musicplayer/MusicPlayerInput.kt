package com.musicplayer

data class MusicPlayerInput(
    val uriString: String,
    val displayTitle: String,
    val clipDurationMills: Long? = null
)
