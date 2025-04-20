package com.simullim.playinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simullim.R
import com.simullim.compose.CommonHeader
import com.simullim.compose.CommonHeaderIcon
import com.simullim.compose.RoundedParkGreenBox

//TODO 백버튼, stop dialog
@Composable
internal fun PlayInfoScreen(
    onClickBack: () -> Unit,
    playInfoViewModel: PlayInfoViewModel = viewModel()
) {
    val playInfoModel by playInfoViewModel.playInfoStateFlow.collectAsStateWithLifecycle()
    val playStatus by playInfoViewModel.playStatusStateFlow.collectAsStateWithLifecycle()
    val playTrack by playInfoViewModel.playTrackStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.screen_padding_dp))
    ) {
        CommonHeader(
            title = stringResource(R.string.play_info_title),
            modifier = Modifier.fillMaxWidth(),
            leftIcon = CommonHeaderIcon(
                drawableRes = com.example.common.R.drawable.baseline_arrow_back_ios_new_24,
                onClick = onClickBack
            )
        )
        RoundedParkGreenBox(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                item {
                    InfoSection(playInfoModel = playInfoModel, modifier = Modifier.fillMaxWidth())
                }
                item {
                    HorizontalDivider(
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                item {
                    TrackProgressSection(
                        playInfoTrack = playTrack,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        //TODO onClick
        ControllerSection(playStatus = playStatus, {}, {}, modifier = Modifier.padding(top = 8.dp))
    }


}

@Composable
@Preview
private fun PlayInfoScreenPreview() {
    PlayInfoScreen(onClickBack = {})
}