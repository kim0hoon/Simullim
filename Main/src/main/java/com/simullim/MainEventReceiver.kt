package com.simullim

import com.simullim.service.model.PlayServiceInputModel

internal interface MainEventReceiver {
    fun handleEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.Play -> onPlay(
                playServiceInputModel = mainEvent.playServiceInputModel,
                onGranted = mainEvent.onGranted
            )
            is MainEvent.SetPlaylist -> onSetPlaylist()
            is MainEvent.Pause -> onPlayPause()
            is MainEvent.Stop -> onPlayStop()
            is MainEvent.Resume -> onPlayResume()
        }
    }

    fun onPlay(playServiceInputModel: PlayServiceInputModel, onGranted: (() -> Unit)?)
    fun onSetPlaylist()
    fun onPlayPause()
    fun onPlayStop()
    fun onPlayResume()
}