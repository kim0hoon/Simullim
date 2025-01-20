package com.simullim.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simullim.R
import com.simullim.common.MenuButton

@Composable
fun MainScreen(onClickStart: () -> Unit, onClickQuit: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        MenuButton(
            onClick = onClickStart,
            buttonText = stringResource(R.string.start),
            modifier = Modifier.fillMaxWidth()
        )
        MenuButton(
            onClick = onClickQuit,
            buttonText = stringResource(R.string.quit),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen({}) { }
}