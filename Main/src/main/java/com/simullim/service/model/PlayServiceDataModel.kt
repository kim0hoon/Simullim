package com.simullim.service.model

import androidx.annotation.FloatRange
import com.simullim.common.PlaylistModel

internal data class PlayServiceDataModel(
    val currentVelocity: Float = 0f,
    val averageVelocity: Float = 0f,
    val totalDistanceMeter: Float = 0f,
    val totalTimeMills: Long = 0L,
    val playlist: PlaylistModel = PlaylistModel(),
    val currentIdx: Int = 0,
    @FloatRange(0.0, 1.0) val currentProgress: Float = 0.0f
)