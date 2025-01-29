package com.example.music_picker.model

import android.net.Uri

data class PlayItem(
    val uri: Uri,
    val title: String,
    val durationString: String,
    val isChecked: Boolean = false
)
