package com.example.bookstoreapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.data.InventoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(item: InventoryItem? = null, onSave: (InventoryItem) -> Unit) {
    var name by remember { mutableStateOf(item?.name ?: "") }
    var category by remember { mutableStateOf(item?.category ?: "") }
    var quantity by remember { mutableStateOf(item?.quantity?.toString() ?: "") }
    var price by remember { mutableStateOf(item?.price?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                if (name.isBlank() || category.isBlank() || quantity.isBlank() || price.isBlank()) {
                    errorMessage = "All fields must be filled."
                } else {
                    val itemToSave = InventoryItem(
                        id = item?.id ?: 0,
                        name = name,
                        category = category,
                        quantity = quantity.toIntOrNull() ?: 0,
                        price = price.toDoubleOrNull() ?: 0.0
                    )
                    onSave(itemToSave)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text("Save")
        }
    }
}