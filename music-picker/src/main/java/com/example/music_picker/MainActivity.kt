package com.example.music_picker

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.example.music_picker.model.PlayItem
import com.simullim.compose.CommonHeader
import com.simullim.compose.CommonHeaderIcon
import com.simullim.compose.RoundedParkGreenBox
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.compose.ui.theme.SimullimTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private var musicPickerObserver: MusicPickerObserver? = null
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimullimTheme {
                Contents()
            }
        }
        musicPickerObserver =
            MusicPickerObserver(activityResultRegistry, MUSIC_PICKER_OBSERVER_KEY) { uris ->
                val metadataRetriever = MediaMetadataRetriever()
                val projection = arrayOf(MediaStore.Audio.Media.DISPLAY_NAME)
                val unknownString = getString(R.string.unknown)
                val playItems = uris.mapNotNull { uri ->
                    metadataRetriever.setDataSource(this, uri)
                    val duration =
                        metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            ?.let {
                                val longDuration = it.toLong()
                                val min = TimeUnit.MILLISECONDS.toMinutes(longDuration)
                                val sec = TimeUnit.MILLISECONDS.toSeconds(longDuration) % 60
                                String.format(null, "%02d:%02d", min, sec)
                            } ?: unknownString
                    val title = contentResolver.query(uri, projection, null, null, null)
                        ?.apply { moveToFirst() }?.let {
                            val displayNameIndex =
                                it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                            val displayName = it.getString(displayNameIndex)
                            it.close()
                            displayName
                        } ?: unknownString
                    uri.path?.let { path ->
                        PlayItem(uriString = path, title = title, durationString = duration)
                    }
                }
                metadataRetriever.release()
                mainViewModel.addPlayItems(playItems)
            }.also {
                lifecycle.addObserver(it)
            }
    }

    @Composable
    private fun Contents() {
        val viewModel = viewModel<MainViewModel>()
        val playItems = viewModel.playItemsStateFlow.collectAsState().value
        Box(modifier = Modifier.background(DarkGrey)) {
            RoundedParkGreenBox(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {
                Column {
                    CommonHeader(
                        title = stringResource(R.string.playlist),
                        leftIcon = CommonHeaderIcon(
                            drawableRes = com.example.common.R.drawable.baseline_arrow_back_ios_new_24,
                            onClick = { finish() }),
                        rightIcon = CommonHeaderIcon(
                            drawableRes = com.example.common.R.drawable.ic_add_24,
                            onClick = { musicPickerObserver?.selectAudio() }
                        )
                    )
                    HorizontalDivider(
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    PlayList(
                        playItems = playItems,
                        onCheckedChanged = viewModel::setCheckedItem,
                        onClickAllCheck = viewModel::checkAllItems,
                        onClickRemove = viewModel::removeCheckedItems,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .fillMaxWidth()
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

    }

    @Composable
    @Preview(showBackground = true)
    private fun ContentsPreview() {
        Contents()
    }

    companion object {
        private const val MUSIC_PICKER_OBSERVER_KEY = "music_picker_observer_key"
    }
}