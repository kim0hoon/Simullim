package com.example.music_picker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayItem(
    val uriString: String,
    val title: String,
    val durationMillis: Long? = null,
    val isChecked: Boolean = false
) : Parcelable {
    val key get() = uriString
}
