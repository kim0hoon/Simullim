package com.simullim.debugtest

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simullim.R
import com.simullim.MainEvent
import com.simullim.MainViewModel
import com.simullim.compose.RoundedParkGreenButton

@Composable
internal fun DebugTestScreen(mainViewModel: MainViewModel = viewModel()) {
    LazyColumn {
        item {
            RoundedParkGreenButton(
                buttonText = stringResource(R.string.debug_test_service_start),
                onClick = {
                    mainViewModel.sendMainEvent(mainEvent = MainEvent.Play(testPlayServiceModel))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            RoundedParkGreenButton(
                buttonText = stringResource(R.string.debug_test_service_pause),
                onClick = {
                    mainViewModel.sendMainEvent(mainEvent = MainEvent.Pause)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            RoundedParkGreenButton(
                buttonText = stringResource(R.string.debug_test_service_resume),
                onClick = {
                    mainViewModel.sendMainEvent(mainEvent = MainEvent.Resume)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            RoundedParkGreenButton(
                buttonText = stringResource(R.string.debug_test_service_stop),
                onClick = {
                    mainViewModel.sendMainEvent(mainEvent = MainEvent.Stop)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}