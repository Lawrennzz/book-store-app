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
fun EditItemScreen(navController: NavController, itemId: Int?, viewModel: InventoryViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    var item by remember { mutableStateOf<Item?>(null) }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(itemId) {
        if (itemId != null) {
            item = viewModel.getItemById(itemId)
            item?.let {
                name = it.name
                category = it.category
                quantity = it.quantity.toString()
                price = it.price.toString()
            }
        }
    }

    if (item == null) {
        Text("Loading...")
        return
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isBlank() || category.isBlank() || quantity.isBlank() || price.isBlank()) {
                    errorMessage = "All fields are required"
                } else if (quantity.toIntOrNull() == null || price.toDoubleOrNull() == null) {
                    errorMessage = "Quantity and Price must be valid numbers"
                } else {
                    scope.launch {
                        viewModel.updateItem(
                            item!!.copy(
                                name = name,
                                category = category,
                                quantity = quantity.toInt(),
                                price = price.toDouble()
                            )
                        )
                        navController.popBackStack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update")
        }
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}