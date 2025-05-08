package com.musicplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicPlayer(context: Context) {
    private var player = ExoPlayer.Builder(context).build()
    private val _statusStateFlow = MutableStateFlow<Status>(Status.Initialized)
    val statusStateFlow = _statusStateFlow.asStateFlow()

    init {
        initPlayer()
    }

    private fun initPlayer() {
        player.run {
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    _statusStateFlow.value = Status.Error(error)
                }
            })
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
    }

    fun setMediaItems(uriStrings: List<String>) {
        val mediaItems = uriStrings.map { MediaItem.fromUri(it) }
        player.setMediaItems(mediaItems)
        _statusStateFlow.value = Status.Ready
    }

    fun play() {
        player.play()
        _statusStateFlow.value = Status.Playing
    }

    fun pause() {
        player.pause()
        _statusStateFlow.value = Status.Paused
    }

    fun stop() {
        player.stop()
        _statusStateFlow.value = Status.Paused
    }

    fun release() {
        player.release()
        _statusStateFlow.value = Status.Released
    }
}