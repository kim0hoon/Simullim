package com.example.music_picker

import androidx.lifecycle.ViewModel
import com.example.music_picker.model.PlayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class MainViewModel : ViewModel() {
    private val _playItemsStateFlow = MutableStateFlow<List<PlayItem>>(emptyList())
    val playItemsStateFlow = _playItemsStateFlow.asStateFlow()

    fun setPlayItems(items: List<PlayItem>) {
        _playItemsStateFlow.value = items
    }

    fun addPlayItems(items: List<PlayItem>) {
        _playItemsStateFlow.update {
            it.toMutableList().apply {
                addAll(items)
            }
        }
    }

    fun removePlayItems(items: List<PlayItem>) {
        _playItemsStateFlow.update {
            it.toMutableList().apply {
                removeAll(items)
            }
        }
    }

    fun clearPlayItems() {
        _playItemsStateFlow.value = emptyList()
    }

    fun setCheckedItem(index: Int, isChecked: Boolean) {
        _playItemsStateFlow.update {
            val target = it.getOrNull(index) ?: return
            it.toMutableList().apply {
                set(index, target.copy(isChecked = isChecked))
            }
        }
    }
}