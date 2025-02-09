package com.simullim

internal interface MainEventReceiver {
    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            MainEvent.PLAY -> onPlay()
            MainEvent.SET_PLAYLIST -> onSetPlaylist()
        }
    }

    fun onPlay()
    fun onSetPlaylist()
}