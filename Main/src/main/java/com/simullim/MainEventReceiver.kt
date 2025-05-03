package com.simullim

import com.simullim.service.model.PlayServiceModel

internal interface MainEventReceiver {
    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.Play -> onPlay(
                playServiceModel = mainEvent.playServiceModel,
                onGranted = mainEvent.onGranted
            )
            is MainEvent.SetPlaylist -> onSetPlaylist()
            is MainEvent.Pause -> onPlayPause()
            is MainEvent.Stop -> onPlayStop()
            is MainEvent.Resume -> onPlayResume()
        }
    }

    fun onPlay(playServiceModel: PlayServiceModel, onGranted: (() -> Unit)?)
    fun onSetPlaylist()
    fun onPlayPause()
    fun onPlayStop()
    fun onPlayResume()
}