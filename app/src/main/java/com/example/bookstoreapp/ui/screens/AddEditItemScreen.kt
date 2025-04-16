package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookstoreapp.data.entity.Item
import com.example.bookstoreapp.ui.components.InputField
import com.example.bookstoreapp.ui.viewmodel.ItemViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(
    viewModel: ItemViewModel,
    navController: NavController,
    itemId: Int? = null
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(itemId) {
        itemId?.let { id ->
            viewModel.getItemById(id).collectLatest { item ->
                name = item.name
                category = item.category
                quantity = item.quantity.toString()
                price = item.price.toString()
            }
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (itemId == null) "Add Item" else "Edit Item") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InputField(value = name, onValueChange = { name = it }, label = "Name")
            InputField(value = category, onValueChange = { category = it }, label = "Category")
            InputField(value = quantity, onValueChange = { quantity = it }, label = "Quantity")
            InputField(value = price, onValueChange = { price = it }, label = "Price")

            Button(
                onClick = {
                    if (itemId == null) {
                        viewModel.addItem(name, category, quantity, price)
                    } else {
                        viewModel.updateItem(
                            Item(
                                id = itemId,
                                name = name,
                                category = category,
                                quantity = quantity.toIntOrNull() ?: 0,
                                price = price.toDoubleOrNull() ?: 0.0
                            )
                        )
                    }
                    if (viewModel.errorMessage.value.isEmpty()) {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}