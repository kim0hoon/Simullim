package com.simullim.playinfo

import androidx.lifecycle.ViewModel
import com.simullim.playinfo.model.PlayInfoModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class PlayInfoViewModel : ViewModel() {
    private val _playInfoStateFlow = MutableStateFlow<PlayInfoModel>(PlayInfoModel())
    val playInfoStateFlow = _playInfoStateFlow.asStateFlow()
}