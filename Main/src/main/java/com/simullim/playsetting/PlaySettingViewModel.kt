package com.simullim.playsetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simullim.common.PaceSetting
import com.simullim.common.PlaylistModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

internal class PlaySettingViewModel : ViewModel() {
    private val _playListStateFlow = MutableStateFlow(PlaylistModel())
    val playListStateFlow = _playListStateFlow.asStateFlow()

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

    fun setPlaylist(playlist: List<PlaylistModel.Playlist>) {
        _playListStateFlow.value = PlaylistModel(playlist = playlist)
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

    private fun getUpdatedStart(old: List<PaceSetting.Pace>): List<PaceSetting.Pace> {
        var acc = 0
        return old.map {
            it.copy(start = acc).also { model ->
                acc += model.length
            }
        }
    }

    fun addNewPace() {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        val start = currentPaceList.lastOrNull()?.run { start + length } ?: 0
        updateCurrentPaceList(currentPaceList + PaceSetting.Pace(start = start))
    }

    fun removePaceAt(idx: Int) {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        val target = currentPaceList.getOrNull(idx) ?: return
        val updated = getUpdatedStart(old = currentPaceList - target)
        updateCurrentPaceList(updated)

    }

    fun updatePaceLength(idx: Int, length: Int) {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        val updated = currentPaceList.toMutableList().also {
            it.getOrNull(idx) ?: return
            it[idx] = it[idx].copy(length = length)
        }
        updateCurrentPaceList(paceList = getUpdatedStart(old = updated))
    }

    fun updatePaceVelocity(idx: Int, velocity: Int) {
        val currentPaceList = currentPaceListStateFlow.value.paceList
        val updated = currentPaceList.toMutableList().also {
            it.getOrNull(idx) ?: return
            it[idx] = it[idx].copy(velocitySecPerKiloMeter = velocity)
        }
        updateCurrentPaceList(paceList = getUpdatedStart(old = updated))
    }
}