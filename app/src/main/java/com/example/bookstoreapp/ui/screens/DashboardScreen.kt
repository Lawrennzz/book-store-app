package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bookstore Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("inventory") }) {
            Text("View Inventory")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("add") }) {
            Text("Add Item")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("settings") }) {
            Text("Settings")
        }
    }
}