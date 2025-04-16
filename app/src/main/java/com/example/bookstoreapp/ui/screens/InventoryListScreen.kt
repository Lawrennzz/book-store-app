package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookstoreapp.data.entity.Item
import com.example.bookstoreapp.ui.viewmodel.InventoryViewModel

@Composable
fun InventoryListScreen(navController: NavController, viewModel: InventoryViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val items by viewModel.allItems.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by name or ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            LazyColumn {
                items(items.filter {
                    it.name.contains(searchQuery, ignoreCase = true) || it.id.toString().contains(searchQuery)
                }) { item ->
                    ItemRow(item = item, onClick = { navController.navigate("detail/${item.id}") })
                }
            }
        }
    }
}

@Composable
fun ItemRow(item: Item, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Category: ${item.category}")
            Text(text = "Stock: ${item.quantity}")
            Text(text = "Price: RM ${item.price}")
        }
    }
}