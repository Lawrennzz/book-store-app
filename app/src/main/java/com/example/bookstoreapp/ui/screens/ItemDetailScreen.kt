package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookstoreapp.data.entity.Item
import com.example.bookstoreapp.ui.viewmodel.InventoryViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemDetailScreen(navController: NavController, itemId: Int?, viewModel: InventoryViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    var item by remember { mutableStateOf<Item?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(itemId) {
        if (itemId != null) {
            item = viewModel.getItemById(itemId)
        }
    }

    if (item == null) {
        Text("Loading...")
        return
    }

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(text = item!!.name, style = MaterialTheme.typography.headlineMedium)
            Text(text = "ID: ${item!!.id}")
            Text(text = "Category: ${item!!.category}")
            Text(text = "Stock: ${item!!.quantity}")
            Text(text = "Price: RM ${item!!.price}")
            Text(text = "Total Value: RM ${item!!.quantity * item!!.price}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("edit/${item!!.id}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit")
            }
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete ${item!!.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        viewModel.deleteItem(item!!)
                        navController.popBackStack()
                    }
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}