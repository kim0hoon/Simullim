package com.simullim

internal interface MainEventReceiver {
    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.Play -> onPlay(onGranted = mainEvent.onGranted)
            is MainEvent.SetPlaylist -> onSetPlaylist()
            is MainEvent.Pause -> onPlayPause()
            is MainEvent.Stop -> onPlayStop()
            is MainEvent.Resume -> onPlayResume()
        }
    }

    fun onPlay(onGranted: (() -> Unit)?)
    fun onSetPlaylist()
    fun onPlayPause()
    fun onPlayStop()
    fun onPlayResume()
}