package com.example.bookstoreapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.InventoryItem
import com.example.bookstoreapp.data.InventoryItemDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(private val dao: InventoryItemDao) : ViewModel() {
    val inventoryItems: StateFlow<List<InventoryItem>> = dao.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addItem(item: InventoryItem) {
        viewModelScope.launch {
            dao.insert(item)
        }
    }

    fun updateItem(item: InventoryItem) {
        viewModelScope.launch {
            dao.update(item)
        }
    }

    fun deleteItem(item: InventoryItem) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}