package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookstoreapp.data.entity.Item
import com.example.bookstoreapp.ui.viewmodel.ItemViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(viewModel: ItemViewModel, itemId: Int, navController: NavController) {
    var item by remember { mutableStateOf<Item?>(null) }

    LaunchedEffect(itemId) {
        viewModel.getItemById(itemId).collectLatest { item = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Item Details") })
        }
    ) { padding ->
        item?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Name: ${it.name}", style = MaterialTheme.typography.titleLarge)
                Text(text = "Category: ${it.category}")
                Text(text = "Quantity: ${it.quantity}")
                Text(text = "Price: $${it.price}")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* TODO: Navigate to edit screen */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit")
                    }
                    Button(
                        onClick = {
                            viewModel.deleteItem(it)
                            navController.popBackStack()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                }
            }
        } ?: Text("Loading...", modifier = Modifier.padding(padding))
    }
}