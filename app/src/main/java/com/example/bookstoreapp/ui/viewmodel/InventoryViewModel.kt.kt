package com.example.bookstoreapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.database.AppDatabase
import com.example.bookstoreapp.data.entity.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    private val itemDao = AppDatabase.getDatabase(application).itemDao()
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insertItem(item)
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }

    fun searchItems(query: String): Flow<List<Item>> {
        return itemDao.searchItems("%$query%")
    }

    suspend fun getItemById(id: Int): Item? {
        return itemDao.getItemById(id)
    }
}