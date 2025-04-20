package com.simullim

internal interface MainEventReceiver {
    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            MainEvent.PLAY -> onPlay()
            MainEvent.SET_PLAYLIST -> onSetPlaylist()
            MainEvent.PAUSE -> onPlayPause()
            MainEvent.STOP -> onPlayStop()
        }
    }

    fun onPlay()
    fun onSetPlaylist()
    fun onPlayPause()
    fun onPlayStop()
}