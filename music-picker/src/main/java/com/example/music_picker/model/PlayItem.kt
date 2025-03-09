package com.example.music_picker.model

internal data class PlayItem(
    val musicModel: MusicModel,
    val isChecked: Boolean = false
)
