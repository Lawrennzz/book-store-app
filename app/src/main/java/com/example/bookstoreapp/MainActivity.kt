package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookstoreapp.ui.screens.AddEditItemScreen
import com.example.bookstoreapp.ui.screens.ItemDetailScreen
import com.example.bookstoreapp.ui.screens.MainScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.bookstoreapp.modules.appModule
import com.example.bookstoreapp.ui.viewmodel.ItemViewModel
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            modules(appModule)
        }
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel: ItemViewModel by viewModel()
                NavHost(navController, startDestination = "main") {
                    composable("main") { MainScreen(viewModel, navController) }
                    composable("addEditItem") { AddEditItemScreen(viewModel, navController) }
                    composable("addEditItem/{itemId}") { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getString("itemId")?.toInt()
                        AddEditItemScreen(viewModel, navController, itemId)
                    }
                    composable("itemDetail/{itemId}") { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getString("itemId")?.toInt() ?: 0
                        ItemDetailScreen(viewModel, itemId, navController)
                    }
                }
            }
        }
    }
}