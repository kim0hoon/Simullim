package com.simullim

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _pageStateFlow = MutableStateFlow(Page.MAIN)
    val pageStateFlow = _pageStateFlow.asStateFlow()

    fun setPage(page: Page) {
        _pageStateFlow.value = page
    }
}