package com.example.bookstoreapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.bookstoreapp.ui.state.SortOrder

@Composable
fun SortingMenu(
    currentSort: SortOrder,
    onSortSelected: (SortOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = "Sort"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        SortOrder.values().forEach { sortOrder ->
            DropdownMenuItem(
                text = {
                    Text(
                        when (sortOrder) {
                            SortOrder.NAME -> "Name"
                            SortOrder.CATEGORY -> "Category"
                            SortOrder.PRICE_ASC -> "Price (Low to High)"
                            SortOrder.PRICE_DESC -> "Price (High to Low)"
                            SortOrder.QUANTITY_LOW -> "Low Stock First"
                        }
                    )
                },
                onClick = {
                    onSortSelected(sortOrder)
                    expanded = false
                },
                leadingIcon = {
                    if (currentSort == sortOrder) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    }
} 