package com.simullim.service

import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import com.example.gps_tracker.GpsTracker

internal class PlayService : Service() {
    private val gpsTracker by lazy {
        GpsTracker(getSystemService(LocationManager::class.java))
    }

    override fun onBind(intent: Intent?): IBinder = PlayServiceBinder()

    fun start() {

    }

    fun pause() {

    }

    fun stop() {

    }

    inner class PlayServiceBinder : Binder() {
        fun getService() = this@PlayService
    }
}