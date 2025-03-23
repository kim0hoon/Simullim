package com.simullim.start

import androidx.lifecycle.ViewModel
import com.simullim.start.model.PaceSetting
import com.simullim.start.model.PaceSettingModel
import com.simullim.start.model.StartPlayListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class StartViewModel : ViewModel() {
    private val _startPlayListStateFlow = MutableStateFlow(StartPlayListModel())
    val startPlayListStateFlow = _startPlayListStateFlow.asStateFlow()

    private val _paceSettingStateFlow = MutableStateFlow(PaceSettingModel())
    val paceSettingStateFlow = _paceSettingStateFlow.asStateFlow()

    fun setPlaylist(playlist: List<StartPlayListModel.Playlist>) {
        _startPlayListStateFlow.value = StartPlayListModel(playlist = playlist)
    }

    fun setPaceSettingType(type: PaceSetting.Type) {
        _paceSettingStateFlow.update {
            it.copy(selectedType = type)
        }
    }

    fun setTrackLength(length: Int) {
        _paceSettingStateFlow.update {
            val updated = it.currentModel.withLength(length = length)
            it.withCurrentPaceSetting(updated)
        }
    }
}