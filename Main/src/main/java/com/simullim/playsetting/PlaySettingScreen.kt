package com.simullim.playsetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simullim.R
import com.simullim.MainEvent
import com.simullim.MainViewModel
import com.simullim.common.PaceSetting
import com.simullim.compose.CommonHeader
import com.simullim.compose.CommonHeaderIcon
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.ui.theme.DarkGrey

@Composable
internal fun PlaySettingScreen(
    mainViewModel: MainViewModel = viewModel(),
    playSettingViewModel: PlaySettingViewModel = viewModel(),
    onClickStart: () -> Unit,
    onClickBack: () -> Unit
) {
    val playListModel = playSettingViewModel.playListStateFlow.collectAsStateWithLifecycle().value
    val currentType = playSettingViewModel.paceTypeStateFlow.collectAsStateWithLifecycle().value
    val paceSettingModel =
        playSettingViewModel.currentPaceListStateFlow.collectAsStateWithLifecycle().value
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.screen_padding_dp))
    ) {
        CommonHeader(
            title = stringResource(R.string.play_setting_title),
            modifier = Modifier.fillMaxWidth(),
            leftIcon = CommonHeaderIcon(
                drawableRes = com.example.common.R.drawable.baseline_arrow_back_ios_new_24,
                onClick = onClickBack
            )
        )
        RoundedParkGreenBox(
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(all = 16.dp),
            ) {
                item {
                    PlaylistSection(
                        model = playListModel, modifier = Modifier.padding(bottom = 16.dp),
                        onClick = {
                            mainViewModel.sendMainEvent(MainEvent.SetPlaylist)
                        }
                    )
                }
                item {
                    PlaySettingScreenDivider()
                }
                item {
                    SelectTypeSection(
                        type = currentType,
                        onChecked = playSettingViewModel::setPaceSettingType,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                if (paceSettingModel.paceList.isEmpty()) {
                    item {
                        PaceEmptyItem(modifier = Modifier.fillMaxWidth())
                    }
                } else {
                    itemsIndexed(paceSettingModel.paceList) { index: Int, item: PaceSetting.Pace ->
                        if (index > 0) Spacer(modifier = Modifier.height(4.dp))
                        PaceItem(
                            type = currentType,
                            index = index,
                            pace = item,
                            onLengthChanged = { length ->
                                playSettingViewModel.updatePaceLength(
                                    idx = index,
                                    length = length
                                )
                            },
                            onPaceChanged = { velocity ->
                                playSettingViewModel.updatePaceVelocity(
                                    idx = index,
                                    velocity = velocity
                                )
                            },
                            onClickRemoved = { playSettingViewModel.removePaceAt(idx = index) },
                        )
                    }
                }
                item {
                    AddPaceButton(
                        onClick = playSettingViewModel::addNewPace,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    PlaySettingScreenDivider(modifier = Modifier.padding(vertical = 16.dp))
                }
                item {
                    SummarySection(paceSetting = paceSettingModel)
                }
            }
        }

        PlayButton(
            onClick = onClickStart,
            isEnabled = playListModel.playlist.isNotEmpty() && paceSettingModel.paceList.run {
                sumOf { it.length } > 0 && sumOf { it.velocitySecPerKiloMeter } > 0
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(48.dp)

        )
    }

}

@Composable
@Preview
private fun PlaySettingScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = DarkGrey)
    ) {
        PlaySettingScreen(onClickStart = {}, onClickBack = {})
    }
}