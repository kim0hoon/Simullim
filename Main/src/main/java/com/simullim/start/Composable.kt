package com.simullim.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.simullim.compose.CheckableRoundedParkGreenButton
import com.simullim.compose.NumberTextInputDialog
import com.simullim.compose.NumberTextInputDialogParam
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.RoundedParkGreenButton
import com.simullim.compose.ui.theme.GreyD4
import com.simullim.compose.ui.theme.Typography
import com.simullim.meterToKiloMeterMeterString
import com.simullim.millsToHourMinSecString
import com.simullim.millsToMinSecString
import com.simullim.secToHourMinSecString
import com.simullim.secToMinSecString
import com.simullim.start.model.PaceSetting
import com.simullim.start.model.StartPlayListModel

//TODO pace clear, pace remove 추가
@Composable
internal fun StartScreenDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        color = Color.White
    )
}

@Composable
internal fun StartTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        color = Color.White,
        style = Typography.headlineMedium,
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
        StartTitle(
            title = stringResource(R.string.start_playlist_title),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        RoundedParkGreenBox(
            modifier = Modifier
                .fillMaxWidth()
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

@Composable
internal fun SelectTypeSection(
    type: PaceSetting.Type,
    onChecked: (PaceSetting.Type) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        StartTitle(
            title = stringResource(R.string.start_play_setting_length_title),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
}

@Composable
@Preview
private fun SelectTypeSectionPreview() {
    SelectTypeSection(type = PaceSetting.Type.Default, onChecked = {})
}

@Composable
internal fun TimeInputDialog(
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val params = arrayOf(
        NumberTextInputDialogParam(
            toResult = { it * 3600 },
            text = stringResource(R.string.hour2),
            range = 0..1000
        ),
        NumberTextInputDialogParam(
            toResult = { it * 60 },
            text = stringResource(R.string.minute),
            range = 0..59
        ),
        NumberTextInputDialogParam(
            toResult = { it },
            text = stringResource(R.string.second),
            range = 0..59
        )
    )
    NumberTextInputDialog(
        title = stringResource(R.string.time_input_dialog_title),
        params = params,
        confirmText = stringResource(R.string.confirm),
        onConfirm = onConfirm,
        cancelText = stringResource(R.string.cancel),
        onCancel = onDismiss,
        modifier = modifier
    )
}

@Composable
internal fun DistanceInputDialog(
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val params = arrayOf(
        NumberTextInputDialogParam(
            toResult = { it * 1000 },
            text = stringResource(R.string.kilo_meter),
            range = 0..1000
        ),
        NumberTextInputDialogParam(
            toResult = { it }, text = stringResource(R.string.meter),
            range = 0..999
        ),
    )
    NumberTextInputDialog(
        params = params,
        title = stringResource(R.string.distance_input_dialog_title),
        confirmText = stringResource(R.string.confirm),
        onConfirm = onConfirm,
        cancelText = stringResource(R.string.cancel),
        onCancel = onDismiss,
        modifier = modifier
    )
}

@Composable
internal fun PaceInputDialog(
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val params = arrayOf(
        NumberTextInputDialogParam(
            toResult = { it * 60 },
            text = stringResource(R.string.minute),
            range = 0..59
        ),
        NumberTextInputDialogParam(
            toResult = { it },
            text = stringResource(R.string.second),
            range = 0..59
        )
    )
    NumberTextInputDialog(
        title = stringResource(R.string.pace_input_dialog_title),
        params = params,
        confirmText = stringResource(R.string.confirm),
        onConfirm = onConfirm,
        cancelText = stringResource(R.string.cancel),
        onCancel = onDismiss,
        modifier = modifier
    )
}

private enum class DialogType {
    TimeInput,
    DistanceInput,
    PaceInput,
    Nothing
}

@Composable
internal fun PaceItem(
    type: PaceSetting.Type,
    index: Int,
    start: Int,
    pace: PaceSetting.Pace,
    onLengthChanged: (Int) -> Unit,
    onPaceChanged: (Int) -> Unit,
    onClickRemoved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val toDisplayString = when (type) {
        PaceSetting.Type.Time -> ::secToHourMinSecString
        PaceSetting.Type.Distance -> ::meterToKiloMeterMeterString
    }
    var showInputDialog by remember { mutableStateOf(DialogType.Nothing) }
    val onDismiss = { showInputDialog = DialogType.Nothing }

    RoundedParkGreenBox(modifier = modifier) {
        when (showInputDialog) {
            DialogType.PaceInput -> PaceInputDialog(
                onConfirm = onPaceChanged,
                onDismiss = onDismiss
            )
            DialogType.TimeInput -> TimeInputDialog(
                onConfirm = onLengthChanged,
                onDismiss = onDismiss
            )
            DialogType.DistanceInput -> DistanceInputDialog(
                onConfirm = onLengthChanged,
                onDismiss = onDismiss
            )
            DialogType.Nothing -> {
                //do nothing
            }
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.start_play_setting_pace_interval, index + 1),
                        color = Color.White,
                        style = Typography.titleLarge,
                    )
                    Text(
                        text = "${toDisplayString(start.toLong())} ~ ${toDisplayString(start.toLong() + pace.length)}",
                        color = Color.LightGray,
                        style = Typography.bodyLarge,
                    )
                }
                Image(
                    painter = painterResource(com.example.common.R.drawable.baseline_delete_24),
                    contentDescription = "remove pace",
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier.clickable {
                        onClickRemoved()
                    }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            RoundedParkGreenBox(modifier = Modifier.padding(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.start_play_setting_pace_interval_length),
                        color = Color.White,
                        style = Typography.titleMedium
                    )
                    Text(
                        text = toDisplayString(pace.length.toLong()),
                        style = Typography.bodyLarge,
                        color = Color.LightGray,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                    Image(
                        painter = painterResource(com.example.common.R.drawable.baseline_edit_24),
                        contentDescription = "edit length",
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier.clickable {
                            showInputDialog = when (type) {
                                PaceSetting.Type.Time -> DialogType.TimeInput
                                PaceSetting.Type.Distance -> DialogType.DistanceInput
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            RoundedParkGreenBox(modifier = Modifier.padding(8.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.start_play_setting_pace_interval_velocity),
                        color = Color.White,
                        style = Typography.titleMedium
                    )
                    Text(
                        text = secToMinSecString(pace.length.toLong()) + "/km",
                        style = Typography.bodyLarge,
                        color = Color.LightGray,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                    Image(
                        painter = painterResource(com.example.common.R.drawable.baseline_edit_24),
                        contentDescription = "edit velocity",
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier.clickable {
                            showInputDialog = DialogType.PaceInput
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun PaceItemPreview() {
    PaceItem(
        type = PaceSetting.Type.Distance,
        index = 12,
        start = 10312,
        pace = PaceSetting.Pace(0, 312),
        onLengthChanged = {},
        onPaceChanged = {},
        onClickRemoved = {},
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
internal fun PaceEmptyItem(modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier) {
        Text(
            text = stringResource(R.string.start_play_setting_pace_empty),
            color = Color.White,
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }
}

@Composable
@Preview
private fun PaceEmptyItemPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        PaceEmptyItem()
    }
}

@Composable
internal fun AddPaceButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    RoundedParkGreenButton(
        onClick = onClick,
        buttonText = stringResource(R.string.start_play_setting_pace_add),
        modifier = modifier
    )
}

@Composable
@Preview
private fun AddPaceButtonPreview() {
    AddPaceButton({})
}

@Composable
internal fun PlayButton(onClick: () -> Unit, isEnabled: Boolean, modifier: Modifier = Modifier) {
    RoundedParkGreenButton(
        onClick = onClick,
        buttonText = stringResource(R.string.start),
        isEnabled = isEnabled,
        modifier = modifier
    )
}

@Composable
@Preview
private fun PlayButtonPreview() {
    PlayButton({}, true)
}