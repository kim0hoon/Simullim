package com.simullim

import android.app.Application
import com.example.simullim.BuildConfig
import timber.log.Timber

class SimullimApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}