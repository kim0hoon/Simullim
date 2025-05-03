package com.simullim

import com.simullim.service.model.PlayServiceModel

internal sealed interface MainEvent {
    data class Play(val playServiceModel: PlayServiceModel, val onGranted: (() -> Unit)? = null) :
        MainEvent

    data object Pause : MainEvent
    data object Stop : MainEvent
    data object Resume : MainEvent
    data object SetPlaylist : MainEvent
}