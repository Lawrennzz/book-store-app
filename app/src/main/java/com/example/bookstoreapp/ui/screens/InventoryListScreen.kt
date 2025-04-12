package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstoreapp.data.InventoryItem
import com.example.bookstoreapp.ui.components.*
import com.example.bookstoreapp.ui.state.InventoryEvent
import com.example.bookstoreapp.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryListScreen(
    onNavigateToAddItem: () -> Unit,
    onNavigateToItemDetail: (Int) -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.getAllCategories().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            Column {
                TopBar(
                    title = "Inventory",
                    actions = {
                        SortingMenu(
                            currentSort = uiState.sortOrder,
                            onSortSelected = { viewModel.onEvent(InventoryEvent.Sort(it)) }
                        )
                    }
                )
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onEvent(InventoryEvent.Search(it)) }
                )
                CategoryFilter(
                    categories = categories,
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = { viewModel.onEvent(InventoryEvent.FilterByCategory(it)) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddItem) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingScreen()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(uiState.items) { item ->
                    InventoryItemCard(
                        item = item,
                        onItemClick = { onNavigateToItemDetail(item.id) }
                    )
                }
            }
        }
    }

    // Show error dialog if there's an error
    uiState.error?.let { error ->
        ErrorDialog(
            message = error,
            onDismiss = { viewModel.onEvent(InventoryEvent.ClearError) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InventoryItemCard(
    item: InventoryItem,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Category: ${item.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quantity: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Price: $${String.format("%.2f", item.price)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 