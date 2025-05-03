package com.musicplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class MusicPlayer(context: Context) {
    private var player = ExoPlayer.Builder(context).build()

    init {
        initPlayer()
    }

    private fun initPlayer() {
        //TODO 상태 관리
        player.run {
            addListener(object : Player.Listener {
            })
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
    }

    fun setMediaItems(uriStrings: List<String>) {
        val mediaItems = uriStrings.map { MediaItem.fromUri(it) }
        player.setMediaItems(mediaItems)
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun stop() {
        player.stop()
    }

    fun release() {
        player.release()
    }
}