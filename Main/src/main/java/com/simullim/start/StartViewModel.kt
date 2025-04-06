package com.simullim.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simullim.start.model.PaceSetting
import com.simullim.start.model.StartPlayListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

internal class StartViewModel : ViewModel() {
    private val _startPlayListStateFlow = MutableStateFlow(StartPlayListModel())
    val startPlayListStateFlow = _startPlayListStateFlow.asStateFlow()

    private val _paceTypeStateFlow = MutableStateFlow(PaceSetting.Type.Default)
    val paceTypeStateFlow = _paceTypeStateFlow.asStateFlow()

    private val paceMapStateFlow =
        MutableStateFlow(PaceSetting.Type.entries.associateWith { PaceSetting(type = it) })

    val currentPaceListStateFlow = combine(paceTypeStateFlow, paceMapStateFlow) { type, map ->
        map[type] ?: PaceSetting(paceTypeStateFlow.value)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        PaceSetting(paceTypeStateFlow.value)
    )

    fun setPlaylist(playlist: List<StartPlayListModel.Playlist>) {
        _startPlayListStateFlow.value = StartPlayListModel(playlist = playlist)
    }

    fun setPaceSettingType(type: PaceSetting.Type) {
        _paceTypeStateFlow.value = type
    }

    private fun updateCurrentPaceList(paceList: List<PaceSetting.Pace>) {
        paceMapStateFlow.update {
            val currentType = paceTypeStateFlow.value
            val updated = (it[currentType] ?: return@update it).withPaceList(paceList)
            it.toMutableMap().apply {
                replace(currentType, updated)
            }
        }
    }

    fun addNewPace() {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        updateCurrentPaceList(currentPaceList + PaceSetting.Pace())
    }

    fun removePaceAt(idx: Int) {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        val target = currentPaceList.getOrNull(idx) ?: return
        updateCurrentPaceList(currentPaceList - target)

    }

    fun updatePaceLength(idx: Int, length: Int) {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        val updated = currentPaceList.toMutableList().also {
            it.getOrNull(idx) ?: return
            it[idx] = it[idx].copy(length = length)
        }
        updateCurrentPaceList(paceList = updated)
    }

    fun updatePaceVelocity(idx: Int, velocity: Int) {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        val updated = currentPaceList.toMutableList().also {
            it.getOrNull(idx) ?: return
            it[idx] = it[idx].copy(velocitySecPerKiloMeter = velocity)
        }
        updateCurrentPaceList(paceList = updated)
    }
}