package com.simullim.service.model

import com.simullim.common.PaceSetting
import com.simullim.common.PlaylistModel

internal data class PlayServiceModel(
    val playlistModel: PlaylistModel,
    val paceSetting: PaceSetting
)
