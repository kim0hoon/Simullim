package com.example.music_picker

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import com.example.music_picker.model.PlayItem

class MusicPickerResultContract : ActivityResultContract<Unit, List<PlayItem>?>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(context, MainActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): List<PlayItem>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableArrayListExtra(PLAYLIST_RESULT_KEY, PlayItem::class.java)?.toList()
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableArrayListExtra<PlayItem>(PLAYLIST_RESULT_KEY)?.toList()
        }
    }
}