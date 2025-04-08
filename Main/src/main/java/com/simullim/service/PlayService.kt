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

internal class PlayService : Service() {
    private val gpsTracker by lazy {
        GpsTracker(this)
    }
    private val binder = PlayServiceBinder()

    val gpsDataStateFlow get() = gpsTracker.gpsDataStateFlow
    val errorEventFlow get() = gpsTracker.errorEventFlow

    override fun onCreate() {
        super.onCreate()
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

    fun start(onDenied: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onDenied()
            return
        }
        gpsTracker.start()
    }

    fun pause() {
        gpsTracker.pause()
    }

    fun stop() {
        gpsTracker.stop()
    }

    inner class PlayServiceBinder : Binder() {
        fun getService() = this@PlayService
    }

    companion object {
        private const val CHANNEL_ID = "PlayNotificationChannel"
        private const val FOREGROUND_SERVICE_ID = 101
    }
}