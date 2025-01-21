package com.simullim.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simullim.R
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.RoundedParkGreenButton

@Composable
fun MainScreen(onClickStart: () -> Unit, onClickQuit: () -> Unit) {
    RoundedParkGreenBox(modifier = Modifier.fillMaxHeight()) {
        Column(modifier = Modifier.padding(16.dp)) {
            RoundedParkGreenButton(
                onClick = onClickStart,
                buttonText = stringResource(R.string.start),
                modifier = Modifier.fillMaxWidth(),
                innerPaddingVertical = 8.dp,
                innerPaddingHorizontal = 8.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            RoundedParkGreenButton(
                onClick = onClickQuit,
                buttonText = stringResource(R.string.quit),
                modifier = Modifier.fillMaxWidth(),
                innerPaddingVertical = 8.dp,
                innerPaddingHorizontal = 8.dp
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen({}) { }
}