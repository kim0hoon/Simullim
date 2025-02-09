package com.simullim.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

internal class PlayServiceConnection : ServiceConnection {
    var playServiceBinder: PlayService.PlayServiceBinder? = null
        private set

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        playServiceBinder = service as? PlayService.PlayServiceBinder
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        playServiceBinder = null
    }
}