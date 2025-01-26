package com.example.music_picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simullim.compose.CommonHeader
import com.simullim.compose.CommonHeaderIcon
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.ui.theme.SimullimTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimullimTheme {
                Contents()
            }
        }
    }

    @Composable
    private fun Contents() {
        val viewModel = viewModel<MainViewModel>()
        val playItems = viewModel.playItemsStateFlow.collectAsState().value
        RoundedParkGreenBox(modifier = Modifier.fillMaxSize()) {
            Column {
                CommonHeader(
                    title = stringResource(R.string.playlist),
                    leftIcon = CommonHeaderIcon(
                        drawableRes = com.example.common.R.drawable.baseline_arrow_back_ios_new_24,
                        onClick = { finish() }),
                    rightIcon = CommonHeaderIcon(
                        drawableRes = com.example.common.R.drawable.ic_add_24,
                        onClick = {}
                    )
                )
                HorizontalDivider(
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                PlayList(
                    playItems = playItems,
                    onCheckedChanged = viewModel::setCheckedItem,
                    modifier = Modifier.weight(1f)
                )
                HorizontalDivider(
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                //TODO onclick, 시간
                PlaylistSelectButton(
                    count = playItems.count { it.isChecked },
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun ContentsPreview() {
        Contents()
    }
}