package com.simullim.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simullim.R
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.start.model.PaceSetting

@Composable
internal fun StartScreen(startViewModel: StartViewModel = viewModel()) {
    val playListModel = startViewModel.startPlayListStateFlow.collectAsStateWithLifecycle().value
    val currentType = startViewModel.paceTypeStateFlow.collectAsStateWithLifecycle().value
    val paceSettingModel =
        startViewModel.currentPaceListStateFlow.collectAsStateWithLifecycle().value
    RoundedParkGreenBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.screen_padding_dp))
    ) {
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
            item {
                PlaylistSection(
                    model = playListModel, modifier = Modifier.padding(bottom = 16.dp),
                    onClick = {
                        //TODO onClick
                    }
                )
            }
            item {
                StartScreenDivider()
            }
            item {
                SelectTypeSection(
                    type = currentType,
                    onChecked = startViewModel::setPaceSettingType,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            var startAcc = 0
            itemsIndexed(paceSettingModel.paceList) { index: Int, item: PaceSetting.Pace ->
                PaceItem(
                    type = currentType,
                    index = index,
                    start = startAcc,
                    pace = item,
                    onLengthChanged = { length ->
                        startViewModel.updatePaceLength(
                            idx = index,
                            length = length
                        )
                    },
                    onPaceChanged = { velocity ->
                        startViewModel.updatePaceVelocity(
                            idx = index,
                            velocity = velocity
                        )
                    }
                )
                startAcc += item.length
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