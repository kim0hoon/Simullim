package com.simullim.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simullim.R
import com.simullim.compose.CheckableRoundedParkGreenButton
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.RoundedParkGreenButton
import com.simullim.compose.ui.theme.GreyD4
import com.simullim.compose.ui.theme.Typography
import com.simullim.millsToHourMinSecString
import com.simullim.millsToMinSecString
import com.simullim.start.model.PaceSetting
import com.simullim.start.model.StartPlayListModel

@Composable
internal fun StartScreenDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 16.dp),
        color = Color.White
    )
}

@Composable
internal fun StartTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = Color.White,
        style = Typography.titleLarge,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
@Preview
private fun StartTitlePreview() {
    StartTitle("test title 12312312312312398712938712893")
}

@Composable
internal fun PlaylistSection(
    model: StartPlayListModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        StartTitle(title = stringResource(R.string.start_playlist_title))
        RoundedParkGreenBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(250.dp)
        ) {
            if (model.playlist.isEmpty()) PlaylistEmptyItem(modifier = Modifier.fillMaxSize())
            else {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    itemsIndexed(model.playlist) { index, data ->
                        PlaylistItem(playlistModel = data, modifier = Modifier.fillMaxSize())
                    }

                }
            }
        }
        Text(
            text = stringResource(
                R.string.start_playlist_total_duration,
                millsToHourMinSecString(model.totalDurationMills)
            ),
            color = GreyD4,
            textAlign = TextAlign.End,
            style = Typography.bodySmall,
            modifier = Modifier
                .padding(top = 4.dp, end = 4.dp)
                .fillMaxWidth()
        )
        RoundedParkGreenButton(
            onClick = onClick,
            buttonText = stringResource(R.string.start_playlist_select),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
internal fun PlaylistEmptyItem(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(R.string.start_playlist_empty),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            style = Typography.bodySmall
        )
    }
}

@Composable
@Preview
private fun PlaylistSectionPreview() {
    PlaylistSection(model = StartPlayListModel(List(30) {
        StartPlayListModel.Playlist(
            title = "title $it",
            durationMills = it.toLong() * 1000,
            url = ""
        )
    }), onClick = {})
}

@Composable
@Preview
private fun PlaylistSectionEmptyPreview() {
    PlaylistSection(model = StartPlayListModel(), onClick = {})
}

@Composable
internal fun PlaylistItem(
    playlistModel: StartPlayListModel.Playlist,
    modifier: Modifier = Modifier
) {
    RoundedParkGreenBox(modifier = modifier) {
        Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
            Text(
                text = playlistModel.title,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                style = Typography.bodyMedium
            )
            Text(
                text = millsToMinSecString(playlistModel.durationMills),
                color = GreyD4,
                overflow = TextOverflow.Ellipsis,
                style = Typography.bodySmall
            )
        }
    }
}
//TODO 모델 추가
/**
 * 거리
 * 페이스 조절 칸(구간, 페이스)
 * (페이스 그래프)
 */
@Composable
internal fun SelectTypeSection(
    type: PaceSetting.Type,
    onChecked: (PaceSetting.Type) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        CheckableRoundedParkGreenButton(
            onClick = { onChecked(PaceSetting.Type.Distance) },
            buttonText = stringResource(R.string.start_play_setting_type_distance),
            isChecked = type == PaceSetting.Type.Distance,
            modifier = Modifier.weight(1f)
        )
        CheckableRoundedParkGreenButton(
            onClick = { onChecked(PaceSetting.Type.Time) },
            buttonText = stringResource(R.string.start_play_setting_type_time),
            isChecked = type == PaceSetting.Type.Time,
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
@Preview
private fun SelectTypeSectionPreview() {
    SelectTypeSection(type = PaceSetting.Type.Default, onChecked = {})
}