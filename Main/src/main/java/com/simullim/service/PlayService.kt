package com.simullim.service

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.gps_tracker.GpsTracker
import com.example.simullim.R
import com.musicplayer.MusicPlayer
import com.simullim.safeLet
import com.simullim.service.model.PlayServiceModel
import kotlinx.coroutines.flow.MutableStateFlow

//TODO 상태 flow 추가, gps<->미디어 연동작업
internal class PlayService : Service() {
    private var gpsTracker: GpsTracker? = null
    private val binder = PlayServiceBinder()

    val gpsDataStateFlow get() = gpsTracker?.gpsDataStateFlow

    private var musicPlayer: MusicPlayer? = null

    //TODO combine state flow
    val statusStateFlow get() = MutableStateFlow(PlayServiceStatus.Initialized)

    override fun onCreate() {
        super.onCreate()
        gpsTracker = GpsTracker(context = this)
        musicPlayer = MusicPlayer(context = this)
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).build()
        startForeground(FOREGROUND_SERVICE_ID, notification)
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

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun withNotNullServices(block: (gpsTracker: GpsTracker, musicPlayer: MusicPlayer) -> Unit) {
        safeLet(gpsTracker, musicPlayer) { gpsTracker, musicPlayer ->
            block(gpsTracker, musicPlayer)
        }
    }

    fun start(playServiceModel: PlayServiceModel, onDenied: () -> Unit) {
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
                setMediaItems(uriStrings = playServiceModel.playlistModel.playlist.map { it.url })
                play()
            }
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
    }

    fun pause() {
        val hasPermission = ActivityCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) gpsTracker?.pause()
        else gpsTracker?.pauseNotUpdated()

    }

    fun stop() {
        gpsTracker?.stop()
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
        musicPlayer?.release()
        gpsTracker?.release()
        musicPlayer = null
        gpsTracker = null
    }

    companion object {
        private const val CHANNEL_ID = "PlayNotificationChannel"
        private const val FOREGROUND_SERVICE_ID = 101
    }
}