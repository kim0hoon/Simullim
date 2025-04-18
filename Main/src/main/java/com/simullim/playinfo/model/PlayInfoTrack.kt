package com.simullim.playinfo.model

data class PlayInfoTrack(
    val totalProgress: Float = 0f,
    val currentProgress: Float = 0f,
    val trackList: List<String> = emptyList(),
    val currentIndex: Int = 0
) {
    val currentTrackName = trackList.getOrNull(currentIndex)
}
