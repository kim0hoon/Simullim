package com.simullim.common

import androidx.compose.runtime.Immutable

@Immutable
internal data class PlaylistModel(
    val playlist: List<Playlist> = emptyList()
) {
    val totalDurationMills = playlist.sumOf { it.durationMills }

    data class Playlist(
        val title: String,
        val durationMills: Long,
        val url: String
    )
}