package com.simullim

import com.simullim.service.model.PlayServiceInputModel

internal sealed interface MainEvent {
    data class Play(
        val playServiceInputModel: PlayServiceInputModel,
        val onGranted: (() -> Unit)? = null
    ) :
        MainEvent

    data object Pause : MainEvent
    data object Stop : MainEvent
    data object Resume : MainEvent
    data object SetPlaylist : MainEvent
}