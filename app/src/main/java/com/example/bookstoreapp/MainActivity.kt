package com.example.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookstoreapp.navigation.Screen
import com.example.bookstoreapp.ui.screens.AddEditItemScreen
import com.example.bookstoreapp.ui.screens.InventoryListScreen
import com.example.bookstoreapp.ui.screens.ItemDetailScreen
import com.example.bookstoreapp.ui.theme.BookStoreAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookStoreAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.InventoryList.route
                    ) {
                        composable(Screen.InventoryList.route) {
                            InventoryListScreen(
                                onNavigateToAddItem = {
                                    navController.navigate(Screen.AddItem.route)
                                },
                                onNavigateToItemDetail = { itemId ->
                                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                                }
                            )
                        }

                        composable(Screen.AddItem.route) {
                            AddEditItemScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = Screen.EditItem.route,
                            arguments = listOf(
                                navArgument("itemId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
                            AddEditItemScreen(
                                itemId = itemId,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = Screen.ItemDetail.route,
                            arguments = listOf(
                                navArgument("itemId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
                            ItemDetailScreen(
                                itemId = itemId,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToEdit = { id ->
                                    navController.navigate(Screen.EditItem.createRoute(id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 