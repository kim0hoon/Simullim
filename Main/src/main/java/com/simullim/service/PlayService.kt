package com.simullim.service

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.gps_tracker.GpsTracker
import com.example.simullim.R
import com.musicplayer.MusicPlayer
import com.musicplayer.Status
import com.simullim.collectOnLifecycle
import com.simullim.safeLet
import com.simullim.service.model.PlayServiceInputModel
import com.simullim.service.model.PlayServiceStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

internal class PlayService : LifecycleService() {
    private var gpsTracker: GpsTracker? = null
    private val binder = PlayServiceBinder()

    val gpsDataStateFlow get() = gpsTracker?.gpsDataStateFlow

    private var musicPlayer: MusicPlayer? = null

    private val _statusStateFlow =
        MutableStateFlow<PlayServiceStatus>(PlayServiceStatus.Initialized)
    val statusStateFlow = _statusStateFlow.asStateFlow()


    override fun onCreate() {
        super.onCreate()
        gpsTracker = GpsTracker(context = this)
        musicPlayer = MusicPlayer(context = this)
        withNotNullServices { gpsTracker, musicPlayer ->
            combine(
                gpsTracker.statusStateFlow,
                musicPlayer.statusStateFlow
            ) { gpsStatus, musicStatus ->
                if (gpsStatus is com.example.gps_tracker.Status.Error) PlayServiceStatus.Error(
                    gpsStatus.exception
                )
                else if (musicStatus is Status.Error) PlayServiceStatus.Error(musicStatus.exception)
                else null
            }.distinctUntilChanged().collectOnLifecycle(lifecycleOwner = this) {
                it?.let {
                    gpsTracker.stop()
                    musicPlayer.stop()
                    _statusStateFlow.value = it
                }
            }
        }
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).build()
        startForeground(FOREGROUND_SERVICE_ID, notification)
        _statusStateFlow.value = PlayServiceStatus.Ready
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notificationChannel =
            NotificationChannel(
                CHANNEL_ID,
                getString(R.string.play_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private fun withNotNullServices(block: (gpsTracker: GpsTracker, musicPlayer: MusicPlayer) -> Unit) {
        safeLet(gpsTracker, musicPlayer) { gpsTracker, musicPlayer ->
            block(gpsTracker, musicPlayer)
        }
    }

    fun start(playServiceInputModel: PlayServiceInputModel, onDenied: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onDenied()
            return
        }
        withNotNullServices { gpsTracker, musicPlayer ->
            gpsTracker.start()
            musicPlayer.run {
                setMediaItems(uriStrings = playServiceInputModel.playlistModel.playlist.map { it.url })
                play()
            }
            _statusStateFlow.value = PlayServiceStatus.Playing
        }
    }

    fun resume(onDenied: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onDenied()
            return
        }
        withNotNullServices { gpsTracker, musicPlayer ->
            gpsTracker.start()
            musicPlayer.play()
        }
        _statusStateFlow.value = PlayServiceStatus.Playing
    }

    fun pause() {
        val hasPermission = ActivityCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        withNotNullServices { gpsTracker, musicPlayer ->
            if (hasPermission) gpsTracker.pause()
            else gpsTracker.pauseNotUpdated()
            musicPlayer.pause()
        }
        _statusStateFlow.value = PlayServiceStatus.Paused
    }

    fun stop() {
        withNotNullServices { gpsTracker, musicPlayer ->
            gpsTracker.stop()
            musicPlayer.stop()
        }
        _statusStateFlow.value = PlayServiceStatus.Paused
    }

    inner class PlayServiceBinder : Binder() {
        fun getService() = this@PlayService
    }

    override fun onDestroy() {
        release()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        release()
        super.onTaskRemoved(rootIntent)
    }

    private fun release() {
        withNotNullServices { gpsTracker, musicPlayer ->
            musicPlayer.release()
            gpsTracker.release()
            _statusStateFlow.value = PlayServiceStatus.Released
        }
        musicPlayer = null
        gpsTracker = null
    }

    companion object {
        private const val CHANNEL_ID = "PlayNotificationChannel"
        private const val FOREGROUND_SERVICE_ID = 101
    }
}