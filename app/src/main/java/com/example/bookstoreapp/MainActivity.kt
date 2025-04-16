package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookstoreapp.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookStoreApp()
        }
    }
}

@Composable
fun BookStoreApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen(navController) }
        composable("inventory") { InventoryListScreen(navController) }
        composable("add") { AddItemScreen(navController) }
        composable("edit/{itemId}") { backStackEntry ->
            EditItemScreen(navController, backStackEntry.arguments?.getString("itemId")?.toIntOrNull())
        }
        composable("detail/{itemId}") { backStackEntry ->
            ItemDetailScreen(navController, backStackEntry.arguments?.getString("itemId")?.toIntOrNull())
        }
        composable("settings") { SettingsScreen(navController) }
    }
}