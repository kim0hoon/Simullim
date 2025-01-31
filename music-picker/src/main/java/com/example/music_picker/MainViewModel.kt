package com.example.music_picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_picker.model.PlayItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

internal class MainViewModel : ViewModel() {
    private val _playItemsStateFlow =
        MutableStateFlow<LinkedHashMap<String, PlayItem>>(LinkedHashMap())
    val playItemsStateFlow = _playItemsStateFlow.map { it.values.toList() }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(), emptyList()
    )

    fun setPlayItems(items: List<PlayItem>) {
        _playItemsStateFlow.update {
            LinkedHashMap<String, PlayItem>().apply {
                putAll(items.associateBy { it.key })
            }
        }
    }

    fun addPlayItems(items: List<PlayItem>) {
        _playItemsStateFlow.update {
            LinkedHashMap(it).apply {
                items.forEach { item ->
                    putIfAbsent(item.key, item)
                }
            }
        }
    }

    fun removePlayItems(items: List<PlayItem>) {
        _playItemsStateFlow.update {
            LinkedHashMap(it).apply {
                items.forEach { item ->
                    remove(item.key)
                }
            }
        }
    }

    fun clearPlayItems() {
        _playItemsStateFlow.update {
            LinkedHashMap()
        }
    }

    fun setCheckedItem(key: String, isChecked: Boolean) {
        _playItemsStateFlow.update {
            LinkedHashMap(it).apply {
                val prev = get(key) ?: return
                replace(key, prev.copy(isChecked = isChecked))
            }
        }
    }

    fun checkAllItems(isChecked: Boolean) {
        _playItemsStateFlow.update {
            LinkedHashMap(it).apply {
                replaceAll { _, u -> u.copy(isChecked = isChecked) }
            }
        }
    }

    fun removeCheckedItems() {
        removePlayItems(items = playItemsStateFlow.value.filter { it.isChecked })
    }
}