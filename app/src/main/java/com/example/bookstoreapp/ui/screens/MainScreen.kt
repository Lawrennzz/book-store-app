package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookstoreapp.data.entity.Item
import com.example.bookstoreapp.ui.viewmodel.ItemViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ItemViewModel, navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) {
            viewModel.items.collectLatest { items = it }
        } else {
            viewModel.searchItems(searchQuery).collectLatest { items = it }
        }
    }

    Scaffold(
        topBar = {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name or ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addEditItem") }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                ItemCard(item = item, onClick = {
                    navController.navigate("itemDetail/${item.id}")
                })
            }
        }
    }
}

@Composable
fun ItemCard(item: Item, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Category: ${item.category}")
            Text(text = "Quantity: ${item.quantity}")
            Text(text = "Price: $${item.price}")
        }
    }
}