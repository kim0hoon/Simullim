package com.simullim.playinfo

import androidx.lifecycle.ViewModel
import com.simullim.playinfo.model.PlayInfoModel
import com.simullim.playinfo.model.PlayInfoTrack
import com.simullim.playinfo.model.PlayStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class PlayInfoViewModel : ViewModel() {
    private val _playInfoStateFlow = MutableStateFlow(PlayInfoModel())
    val playInfoStateFlow = _playInfoStateFlow.asStateFlow()

    private val _playStatusStateFlow = MutableStateFlow(PlayStatus.PAUSED)
    val playStatusStateFlow = _playStatusStateFlow.asStateFlow()

    private val _playTrackStateFlow = MutableStateFlow(PlayInfoTrack())
    val playTrackStateFlow = _playTrackStateFlow.asStateFlow()
}