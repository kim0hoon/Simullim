package com.simullim.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.gps_tracker.GpsDataModel
import com.example.gps_tracker.GpsTrackError
import com.simullim.collectOnLifecycle

/**
 * PlayService를 관리하는 함수
 */
internal class PlayServiceManager(
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val onGpsDataEmitted: (GpsDataModel) -> Unit,
    private val onErrorEvent: (GpsTrackError) -> Unit
) : DefaultLifecycleObserver {
    private val playServiceConnection = PlayServiceConnection()
    private val service get() = playServiceConnection.service
    private val intent by lazy {
        Intent(context, PlayService::class.java)
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun startService() {
        startForegroundService(context, intent)
        bindService()
        service?.run {
            gpsDataStateFlow.collectOnLifecycle(lifecycleOwner) {
                onGpsDataEmitted(it)
            }
            errorEventFlow.collectOnLifecycle(lifecycleOwner) {
                onErrorEvent(it)
            }
        }
        play()
    }

    fun stopService() {
        unbindService()
        context.stopService(intent)
    }

    fun play() {
        service?.start {
            //TODO onDenied
        }
    }

    fun pause() {
        service?.pause()
    }

    fun stop() {
        service?.stop()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        bindService()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        unbindService()
    }

    private fun bindService() {
        context.bindService(intent, playServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        context.unbindService(playServiceConnection)
    }
}