package com.example.bookstoreapp.ui.state

import com.example.bookstoreapp.data.InventoryItem

data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NAME,
    val selectedCategory: String? = null
)

enum class SortOrder {
    NAME,
    CATEGORY,
    PRICE_ASC,
    PRICE_DESC,
    QUANTITY_LOW
}

sealed class InventoryEvent {
    data class Search(val query: String) : InventoryEvent()
    data class Sort(val order: SortOrder) : InventoryEvent()
    data class FilterByCategory(val category: String?) : InventoryEvent()
    data class DeleteItem(val item: InventoryItem) : InventoryEvent()
    object ClearError : InventoryEvent()
} 