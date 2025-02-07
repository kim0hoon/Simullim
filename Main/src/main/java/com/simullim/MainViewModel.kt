package com.simullim

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _mainEvent = Channel<MainEvent>(capacity = 10)
    val mainEventFlow = _mainEvent.receiveAsFlow()

    fun sendMainEvent(mainEvent: MainEvent) {
        viewModelScope.launch {
            _mainEvent.send(mainEvent)
        }
    }
}