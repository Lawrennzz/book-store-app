package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstoreapp.data.InventoryItem
import com.example.bookstoreapp.viewmodel.InventoryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(
    itemId: Int? = null,
    onNavigateBack: () -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    // Load existing item if editing
    LaunchedEffect(itemId) {
        if (itemId != null) {
            viewModel.getItemById(itemId).collect { item ->
                item?.let {
                    name = it.name
                    category = it.category
                    quantity = it.quantity.toString()
                    price = it.price.toString()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (itemId == null) "Add Item" else "Edit Item") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                isError = isError && name.isEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                isError = isError && category.isEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                isError = isError && quantity.isEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                isError = isError && price.isEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (validateInputs(name, category, quantity, price)) {
                        scope.launch {
                            val item = InventoryItem(
                                id = itemId ?: 0,
                                name = name,
                                category = category,
                                quantity = quantity.toIntOrNull() ?: 0,
                                price = price.toDoubleOrNull() ?: 0.0
                            )
                            if (itemId == null) {
                                viewModel.addItem(item)
                            } else {
                                viewModel.updateItem(item)
                            }
                            onNavigateBack()
                        }
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (itemId == null) "Add Item" else "Update Item")
            }
        }
    }
}

private fun validateInputs(
    name: String,
    category: String,
    quantity: String,
    price: String
): Boolean {
    return name.isNotEmpty() &&
            category.isNotEmpty() &&
            quantity.isNotEmpty() &&
            price.isNotEmpty() &&
            quantity.toIntOrNull() != null &&
            price.toDoubleOrNull() != null
} 