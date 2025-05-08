package com.simullim.playinfo

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simullim.R
import com.simullim.compose.ui.theme.ParkGreen
import com.simullim.compose.ui.theme.Typography
import com.simullim.meterToKiloMeterMeterString
import com.simullim.playinfo.model.PlayInfoModel
import com.simullim.playinfo.model.PlayInfoTrack
import com.simullim.playinfo.model.PlayStatus
import com.simullim.secToHourMinSecString
import com.simullim.secToMinSecString

@Composable
internal fun InfoSection(playInfoModel: PlayInfoModel, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        val timeStr = secToHourMinSecString(playInfoModel.timeSec.toLong())
        InfoContent(
            title = stringResource(R.string.play_info_elapsed_time),
            value = timeStr,
            modifier = Modifier.fillMaxWidth()
        )
        val distanceStr = meterToKiloMeterMeterString(playInfoModel.totalDistanceMeter.toLong())
        InfoContent(
            title = stringResource(R.string.play_info_total_distance),
            value = distanceStr,
            modifier = Modifier.fillMaxWidth()
        )
        val averVelocityStr = secToMinSecString(playInfoModel.averageVelocity.toLong())
        InfoContent(
            title = stringResource(R.string.play_info_average_velocity),
            value = "$averVelocityStr/km",
            modifier = Modifier.fillMaxWidth()
        )
        val currentVelocityStr = secToMinSecString(playInfoModel.velocity.toLong())
        InfoContent(
            title = stringResource(R.string.play_info_current_velocity),
            value = "$currentVelocityStr/km",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun InfoContent(title: String, value: String, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(text = title, style = Typography.titleMedium, color = Color.White)
        Text(
            text = value,
            style = Typography.bodyMedium,
            color = Color.LightGray,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
@Preview
private fun InfoSectionPreview() {
    InfoSection(PlayInfoModel())
}

@Composable
internal fun TrackProgressSection(playInfoTrack: PlayInfoTrack, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.play_info_track_progress),
                color = Color.White,
                style = Typography.titleMedium
            )
            Spacer(modifier = modifier.weight(1f))
            Text(
                text = "${playInfoTrack.totalProgress * 100}%",
                color = Color.White,
                style = Typography.bodyMedium
            )
        }

        TrackProgressBar(
            progress = playInfoTrack.totalProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.play_info_current_progress),
                color = Color.White,
                style = Typography.titleMedium
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${playInfoTrack.currentProgress * 100}%",
                color = Color.White,
                style = Typography.bodyMedium
            )
        }
        TrackProgressBar(
            progress = playInfoTrack.currentProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = playInfoTrack.currentTrackName
                    ?: stringResource(R.string.play_info_unknown_track_name),
                color = Color.LightGray,
                style = Typography.bodyLarge,
                overflow = TextOverflow.Visible,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .basicMarquee(iterations = Int.MAX_VALUE)

            )
            Text(
                text = playInfoTrack.run { "${currentIndex + 1}/${trackList.size}" },
                color = Color.White,
                style = Typography.labelMedium
            )
        }
    }
}

@Composable
private fun TrackProgressBar(progress: Float, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .height(12.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(7.dp)
                )
                .fillMaxWidth(),
            color = ParkGreen,
            trackColor = Color.Transparent,
            gapSize = 0.dp,
            drawStopIndicator = {}
        )
    }

}

@Composable
@Preview
private fun TrackProgressSectionPreview() {
    TrackProgressSection(
        playInfoTrack = PlayInfoTrack(
            totalProgress = 0.4f,
            currentProgress = 0.5f,
            trackList = List(5) { "track name $it".repeat(4) },
            currentIndex = 2,
        )
    )
}

@Composable
private fun WhiteDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(color = Color.White, modifier = modifier)
}

@Composable
internal fun ControllerSection(
    playStatus: PlayStatus,
    onClickPlay: () -> Unit,
    onClickPause: () -> Unit,
    onClickStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        if (playStatus == PlayStatus.PLAYING) PauseButton(
            onClick = onClickPause,
            modifier = Modifier.weight(1f)
        )
        else ResumeButton(onClick = onClickPlay, modifier = Modifier.weight(1f))
        StopButton(onClick = onClickStop, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PauseButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        BaseButton(
            iconDrawableRes = com.example.common.R.drawable.baseline_pause_24,
            iconTintColor = Color.White,
            borderColor = Color.White,
            onClick = onClick
        )
    }
}

@Composable
private fun StopButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        BaseButton(
            iconDrawableRes = com.example.common.R.drawable.baseline_stop_24,
            iconTintColor = Color.Red,
            borderColor = Color.Red,
            onClick = onClick
        )
    }
}

@Composable
private fun ResumeButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        BaseButton(
            iconDrawableRes = com.example.common.R.drawable.baseline_play_arrow_24,
            iconTintColor = Color.White,
            borderColor = Color.White,
            onClick = onClick
        )
    }
}

@Composable
private fun BaseButton(
    @DrawableRes iconDrawableRes: Int,
    iconTintColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(iconDrawableRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = iconTintColor),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview
private fun ButtonPreview() {
    Column {
        PauseButton({})
        WhiteDivider()
        StopButton({})
        WhiteDivider()
        ResumeButton({})
    }
}