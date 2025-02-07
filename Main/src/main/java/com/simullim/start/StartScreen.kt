package com.simullim.start

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simullim.MainEvent
import com.simullim.MainViewModel

@Composable
fun StartScreen(mainViewModel: MainViewModel = viewModel()) {
    Log.d("TESTLOG", "owner : ${LocalViewModelStoreOwner.current}")
    Column {
        Text(
            text = "add playlist",
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier.clickable {
                mainViewModel.sendMainEvent(MainEvent.SET_PLAYLIST)
            })

        Text(
            text = "gps tracking",
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier.clickable {
                mainViewModel.sendMainEvent(MainEvent.PLAY)
            })
    }

}