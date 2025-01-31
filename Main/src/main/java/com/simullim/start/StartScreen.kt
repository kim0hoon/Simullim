package com.simullim.start

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.music_picker.MusicPickerResultContract
import com.simullim.MainViewModel

@Composable
fun StartScreen() {
    val mainViewModel = viewModel<MainViewModel>()
    val result = rememberLauncherForActivityResult(MusicPickerResultContract()) {
        //TODO set playlist
    }
    Text(
        text = "add playlist",
        color = Color.White,
        fontSize = 32.sp,
        modifier = Modifier.clickable {
            result.launch(Unit)
        })
}