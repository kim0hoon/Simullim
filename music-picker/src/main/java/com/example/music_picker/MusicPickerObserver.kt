package com.example.music_picker

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class MusicPickerObserver(
    private val registry: ActivityResultRegistry,
    private val key: String,
    private val onResult: (List<Uri>) -> Unit
) : DefaultLifecycleObserver {
    private var getContent: ActivityResultLauncher<String>? = null
    override fun onCreate(owner: LifecycleOwner) {
        getContent =
            registry.register(key, owner, ActivityResultContracts.GetMultipleContents(), onResult)
    }

    fun selectAudio() {
        getContent?.launch("audio/*")
    }
}