package com.simullim.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simullim.R
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.ui.theme.DarkGrey

@Composable
internal fun StartScreen(startViewModel: StartViewModel = viewModel()) {
    val playListModel = startViewModel.startPlayListStateFlow.collectAsStateWithLifecycle().value
    val paceSettingModel = startViewModel.paceSettingStateFlow.collectAsStateWithLifecycle().value
    RoundedParkGreenBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.screen_padding_dp))
    ) {
        LazyColumn {
            item {
                PlaylistSection(
                    model = playListModel,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    onClick = {}
                )
            }
            item {
                StartScreenDivider()
            }
            item {
                StartTitle(
                    title = stringResource(R.string.start_play_setting_title),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            item {
                SelectTypeSection(
                    type = paceSettingModel.selectedType,
                    onChecked = startViewModel::setPaceSettingType,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
@Preview
private fun StartScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkGrey)
    ) {
        StartScreen()
    }
}