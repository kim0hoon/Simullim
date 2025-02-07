package com.simullim.service

import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import com.example.gps_tracker.GpsTracker

class PlayService : Service() {
    private val gpsTracker = GpsTracker(getSystemService(LocationManager::class.java))
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}