package com.example.bookstoreapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookstoreapp.data.InventoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(inventoryItems: List<InventoryItem>, onItemClick: (InventoryItem) -> Unit, onAddItem: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = inventoryItems.filter { 
        it.name.contains(searchQuery, ignoreCase = true) || it.id.toString() == searchQuery 
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bookstore Inventory") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItem) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by Name or ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems) { item ->
                    ListItem(
                        modifier = Modifier
                            .clickable { onItemClick(item) }
                            .fillMaxWidth(),
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text("Quantity: ${item.quantity}") }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(
        inventoryItems = listOf(
            InventoryItem(id = 1, name = "Book A", category = "Fiction", quantity = 10, price = 9.99),
            InventoryItem(id = 2, name = "Book B", category = "Non-Fiction", quantity = 5, price = 14.99)
        ),
        onItemClick = {},
        onAddItem = {}
    )
}