package com.example.bookstoreapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.InventoryItem
import com.example.bookstoreapp.data.InventoryItemDao
import com.example.bookstoreapp.ui.state.InventoryEvent
import com.example.bookstoreapp.ui.state.InventoryUiState
import com.example.bookstoreapp.ui.state.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val inventoryItemDao: InventoryItemDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NAME)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            combine(
                _searchQuery,
                _sortOrder,
                _selectedCategory,
                inventoryItemDao.getAllItems()
            ) { query, sortOrder, category, items ->
                var result = items
                
                // Apply search filter
                if (query.isNotEmpty()) {
                    result = result.filter { item ->
                        item.name.contains(query, ignoreCase = true) ||
                        item.category.contains(query, ignoreCase = true)
                    }
                }

                // Apply category filter
                if (category != null) {
                    result = result.filter { it.category == category }
                }

                // Apply sorting
                result = when (sortOrder) {
                    SortOrder.NAME -> result.sortedBy { it.name }
                    SortOrder.CATEGORY -> result.sortedBy { it.category }
                    SortOrder.PRICE_ASC -> result.sortedBy { it.price }
                    SortOrder.PRICE_DESC -> result.sortedByDescending { it.price }
                    SortOrder.QUANTITY_LOW -> result.sortedBy { it.quantity }
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        items = result,
                        searchQuery = query,
                        sortOrder = sortOrder,
                        selectedCategory = category
                    )
                }
            }.catch { error ->
                _uiState.update { it.copy(error = error.message) }
            }.collect()
        }
    }

    fun onEvent(event: InventoryEvent) {
        when (event) {
            is InventoryEvent.Search -> {
                _searchQuery.value = event.query
            }
            is InventoryEvent.Sort -> {
                _sortOrder.value = event.order
            }
            is InventoryEvent.FilterByCategory -> {
                _selectedCategory.value = event.category
            }
            is InventoryEvent.DeleteItem -> {
                viewModelScope.launch {
                    try {
                        inventoryItemDao.deleteItem(event.item)
                    } catch (e: Exception) {
                        _uiState.update { it.copy(error = e.message) }
                    }
                }
            }
            is InventoryEvent.ClearError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    fun getItemById(id: Int): Flow<InventoryItem?> {
        return inventoryItemDao.getItemById(id)
    }

    suspend fun addItem(item: InventoryItem): Long {
        return try {
            inventoryItemDao.insertItem(item)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
            -1
        }
    }

    suspend fun updateItem(item: InventoryItem) {
        try {
            inventoryItemDao.updateItem(item)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    fun getAllCategories(): Flow<List<String>> {
        return inventoryItemDao.getAllItems()
            .map { items -> items.map { it.category }.distinct().sorted() }
    }
}