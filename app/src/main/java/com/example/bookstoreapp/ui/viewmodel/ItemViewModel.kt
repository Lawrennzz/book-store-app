package com.example.bookstoreapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.entity.Item
import com.example.bookstoreapp.data.repository.ItemRepository
import com.example.bookstoreapp.util.ValidationUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: ItemRepository) : ViewModel() {
    val items: Flow<List<Item>> = repository.allItems

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun addItem(name: String, category: String, quantity: String, price: String) {
        if (!ValidationUtil.validateItem(name, category, quantity, price)) {
            _errorMessage.value = "Invalid input. Please check all fields."
            return
        }
        viewModelScope.launch {
            try {
                repository.insert(
                    Item(
                        name = name,
                        category = category,
                        quantity = quantity.toInt(),
                        price = price.toDouble()
                    )
                )
                _errorMessage.value = ""
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add item: ${e.message}"
            }
        }
    }

    fun updateItem(item: Item) = viewModelScope.launch {
        try {
            repository.update(item)
            _errorMessage.value = ""
        } catch (e: Exception) {
            _errorMessage.value = "Failed to update item: ${e.message}"
        }
    }

    fun deleteItem(item: Item) = viewModelScope.launch {
        try {
            repository.delete(item)
            _errorMessage.value = ""
        } catch (e: Exception) {
            _errorMessage.value = "Failed to delete item: ${e.message}"
        }
    }

    fun getItemById(itemId: Int): Flow<Item> = repository.getItemById(itemId)

    fun searchItems(query: String): Flow<List<Item>> = repository.searchItems(query)
}