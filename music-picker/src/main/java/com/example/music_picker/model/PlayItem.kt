package com.example.music_picker.model

data class PlayItem(
    val uriString: String,
    val title: String,
    val durationString: String,
    val isChecked: Boolean = false
) {
    val key get() = uriString
}
