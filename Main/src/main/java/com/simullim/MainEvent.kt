package com.simullim

internal sealed interface MainEvent {
    data class Play(val onGranted: (() -> Unit)? = null) : MainEvent
    data object Pause : MainEvent
    data object Stop : MainEvent
    data object Resume : MainEvent
    data object SetPlaylist : MainEvent
}