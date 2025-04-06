package com.example.music_picker

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import com.example.music_picker.model.MusicModel

class MusicPickerResultContract : ActivityResultContract<Unit, List<MusicModel>?>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(context, MainActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): List<MusicModel>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableArrayListExtra(PLAYLIST_RESULT_KEY, MusicModel::class.java)
                ?.toList()
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableArrayListExtra<MusicModel>(PLAYLIST_RESULT_KEY)?.toList()
        }
    }
}