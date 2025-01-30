package com.example.music_picker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.example.music_picker.model.PlayItem
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.RoundedParkGreenButton
import com.simullim.compose.ui.theme.ParkGreen
import com.simullim.compose.ui.theme.Typography


@Composable
internal fun PlayList(
    playItems: List<PlayItem>,
    onCheckedChanged: (String, Boolean) -> Unit,
    onClickAllCheck: (Boolean) -> Unit,
    onClickRemove: () -> Unit,
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
        Column(modifier = modifier) {
            PlayListOptions(
                isChecked = playItems.any { it.isChecked.not() }.not(),
                onClickAllCheck = onClickAllCheck,
                onClickRemove = onClickRemove,
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(contentPadding = PaddingValues(vertical = 4.dp)) {
                itemsIndexed(playItems) { idx, playItem ->
                    PlayListItem(
                        model = playItem,
                        onCheckedChanged = { onCheckedChanged(playItem.key, it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = (if (idx == 0) 0 else 4).dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PlayListEmptyPreview() {
    PlayList(
        emptyList(), { _, _ -> }, {}, {}, modifier = Modifier
            .background(Color.DarkGray)
            .width(400.dp)
            .height(700.dp)
    )
}

@Composable
@Preview(showBackground = true)
private fun PlayListPreview() {
    PlayList(
        List(20) { PlayItem("", "title $it", "00:00") }, { _, _ -> }, {}, {}, modifier = Modifier
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
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
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
}

@Composable
internal fun PlaylistSelectButton(count: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    RoundedParkGreenButton(
        onClick = onClick,
        buttonText = stringResource(R.string.playlist_select, count),
        modifier = modifier,
        isEnabled = count > 0,
        innerPaddingVertical = 4.dp
    )
}

@Composable
@Preview(showBackground = true)
private fun PlaylistSelectButtonPreview() {
    PlaylistSelectButton(0, {})
}

@Composable
internal fun PlayListOptions(
    isChecked: Boolean,
    onClickAllCheck: (Boolean) -> Unit,
    onClickRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ParkGreenWhiteCheckBox(
                isChecked = isChecked,
                onCheckedChanged = onClickAllCheck
            )
            Text(
                text = stringResource(R.string.check_all),
                color = Color.White,
                style = Typography.bodyLarge,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Image(
            painter = painterResource(com.example.common.R.drawable.baseline_delete_24),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onClickRemove() }
        )
    }
}
