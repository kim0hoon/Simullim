package com.simullim.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

internal class PlayServiceConnection(
    private val onConnected: () -> Unit,
    private val onDisConnected: () -> Unit
) : ServiceConnection {
    private var playServiceBinder: PlayService.PlayServiceBinder? = null
    val service get() = playServiceBinder?.getService()

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        playServiceBinder = service as? PlayService.PlayServiceBinder
        onConnected()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        playServiceBinder = null
        onDisConnected()
    }
}