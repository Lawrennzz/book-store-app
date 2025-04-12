package com.example.bookstoreapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.data.InventoryItem

@Composable
fun ItemDetailScreen(item: InventoryItem) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Name: ${item.name}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Category: ${item.category}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Quantity: ${item.quantity}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Price: $${item.price}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}