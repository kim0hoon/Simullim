package com.musicplayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.exoplayer.ExoPlayer
import com.simullim.DATA_EMIT_DELAY_MILLS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicPlayer(context: Context) {
    private var player = ExoPlayer.Builder(context).build()
    private val _statusStateFlow = MutableStateFlow<Status>(Status.Initialized)
    val statusStateFlow = _statusStateFlow.asStateFlow()

    private val _trackInfoFlow = MutableSharedFlow<TrackInfo>()
    val trackInfoFlow = _trackInfoFlow.asSharedFlow()
    private var trackEmitJob: Job? = null

    init {
        initPlayer()
    }

    private fun initPlayer() {
        player.run {
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    _statusStateFlow.value = Status.Error(error)
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        if (trackEmitJob?.isActive == true) return
                        trackEmitJob?.cancel()
                        trackEmitJob = CoroutineScope(Dispatchers.Main).launch {
                            while (true) {
                                _trackInfoFlow.emit(getCurrentTrackInfo())
                                delay(DATA_EMIT_DELAY_MILLS)
                            }
                        }
                    } else trackEmitJob?.cancel()
                }
            })
            repeatMode = REPEAT_MODE_OFF
            prepare()
        }
    }

    fun setMediaItems(musicPlayerInputs: List<MusicPlayerInput>) {
        val mediaItems = musicPlayerInputs.map { input ->
            MediaItem.Builder().setUri(input.uriString).apply {
                val metaData = MediaMetadata.Builder().setDisplayTitle(input.displayTitle).build()
                setMediaMetadata(metaData)
                input.clipDurationMills?.let { clipDurationMills ->
                    setClippingConfiguration(
                        MediaItem.ClippingConfiguration.Builder().setStartPositionMs(0L)
                            .setEndPositionMs(clipDurationMills).build()
                    )
                }
            }.build()
        }
        player.setMediaItems(mediaItems)
        _statusStateFlow.value = Status.Ready
    }

    fun play() {
        player.run {
            this.repeatMode = repeatMode
            play()
        }
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

    private fun getCurrentTrackInfo(): TrackInfo {
        return TrackInfo(
            currentName = player.mediaMetadata.displayTitle?.toString(),
            currentDurationMills = player.currentPosition,
            currentTotalDurationMills = player.duration,
            currentTrackIndex = player.currentMediaItemIndex,
        )
    }
}