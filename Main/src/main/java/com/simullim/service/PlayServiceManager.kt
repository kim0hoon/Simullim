package com.simullim.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.gps_tracker.GpsData
import com.simullim.collectOnLifecycle
import com.simullim.service.model.PlayServiceDataModel
import com.simullim.service.model.PlayServiceInputModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//TODO 상태관리 stateflow 추가
/**
 * PlayService를 관리하는 함수
 */
internal class PlayServiceManager(
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val onGpsDataEmitted: (GpsData) -> Unit,
) : DefaultLifecycleObserver {
    private val playServiceConnection = PlayServiceConnection(
        onConnected = { isBound = true },
        onDisConnected = { isBound = false })
    private val service get() = playServiceConnection.service.takeIf { isBound }
    private val intent by lazy {
        Intent(context, PlayService::class.java)
    }
    private var isBound: Boolean = false

    private val _playDataStateFlow = MutableStateFlow<PlayServiceDataModel>(PlayServiceDataModel())
    val playDataStateFlow = _playDataStateFlow.asStateFlow()

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun startService() {
        startForegroundService(context, intent)
        bindService()
        service?.run {
            gpsDataStateFlow?.collectOnLifecycle(lifecycleOwner) {
                onGpsDataEmitted(it)
            }
        }
    }

    fun stopService() {
        unbindService()
        context.stopService(intent)
    }

    fun play(playServiceInputModel: PlayServiceInputModel) {
        service?.start(playServiceInputModel = playServiceInputModel) {
            //TODO onDenied
        }
    }

    fun pause() {
        service?.pause()
    }

    fun stop() {
        service?.stop()
    }

    fun resume() {
        service?.resume {
            //TODO onDenied
        }
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
        isBound = false
    }
}