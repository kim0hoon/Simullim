package com.example.music_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.example.music_picker.model.PlayItem
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.RoundedParkGreenButton
import com.simullim.compose.ui.theme.ParkGreen
import com.simullim.compose.ui.theme.Typography


@Composable
internal fun PlayList(
    playItems: List<PlayItem>,
    onCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (playItems.isEmpty()) {
        Box(modifier = modifier) {
            Text(
                text = stringResource(R.string.playlist_empty),
                style = Typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyColumn(modifier = modifier) {
            itemsIndexed(playItems) { idx, playItem ->
                PlayListItem(
                    model = playItem,
                    onCheckedChanged = { onCheckedChanged(playItem.key, it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PlayListEmptyPreview() {
    PlayList(
        emptyList(), { _, _ -> }, modifier = Modifier
            .background(Color.DarkGray)
            .width(400.dp)
            .height(700.dp)
    )
}

@Composable
internal fun PlayListItem(
    model: PlayItem,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    RoundedParkGreenBox(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = model.title,
                style = Typography.bodyMedium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            )
            ParkGreenWhiteCheckBox(isChecked = model.isChecked, onCheckedChanged = {
                onCheckedChanged(it)
            })
        }
    }
}

@Composable
@Preview(showBackground = false)
private fun PlayListItemPreview() {
    Column {
        PlayListItem(
            model = PlayItem(
                "",
                "title test 123123123123123123123123123123123123123123123123123",
                "12:12",
                false
            ), { _ -> })

        PlayListItem(
            model = PlayItem(
                "",
                "title test",
                "02:12",
                true
            ), { _ -> })
    }
}

@Composable
internal fun ParkGreenWhiteCheckBox(
    isChecked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Checkbox(
        checked = isChecked, onCheckedChange = {
            onCheckedChanged(it)
        }, modifier = modifier, colors = CheckboxDefaults.colors().copy(
            checkedCheckmarkColor = ParkGreen,
            uncheckedBoxColor = Color.Transparent,
            checkedBoxColor = Color.Transparent,
            checkedBorderColor = Color.White,
            uncheckedBorderColor = Color.White
        )
    )
}

@Composable
internal fun PlaylistSelectButton(count: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    RoundedParkGreenButton(
        onClick = onClick,
        buttonText = stringResource(R.string.playlist_select, count),
        modifier = modifier,
        isEnabled = count > 0
    )
}

@Composable
@Preview(showBackground = true)
private fun PlaylistSelectButtonPreview() {
    PlaylistSelectButton(0, {})
}