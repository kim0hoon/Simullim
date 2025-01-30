package com.example.music_picker.model

data class PlayItem(
    val uriString: String,
    val title: String,
    val durationMillis: Long? = null,
    val isChecked: Boolean = false
) {
    val key get() = uriString
}
