package com.simullim.start

import androidx.lifecycle.ViewModel
import com.simullim.start.model.StartPlayListModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class StartViewModel : ViewModel() {
    private val _startPlayListStateFlow = MutableStateFlow<List<StartPlayListModel>>(emptyList())
    val startPlayListStateFlow = _startPlayListStateFlow.asStateFlow()

    fun setPlaylist(playlist: List<StartPlayListModel>) {
        _startPlayListStateFlow.value = playlist
    }
}