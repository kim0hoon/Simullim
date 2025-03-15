package com.example.music_picker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicModel(
    val uriString: String,
    val title: String,
    val durationMillis: Long? = null
) : Parcelable {
    val key get() = uriString
}
