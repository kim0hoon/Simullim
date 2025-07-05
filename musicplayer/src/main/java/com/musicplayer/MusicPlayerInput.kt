package com.musicplayer

data class MusicPlayerInput(
    val uriString: String,
    val clipDurationMills: Long? = null
)
