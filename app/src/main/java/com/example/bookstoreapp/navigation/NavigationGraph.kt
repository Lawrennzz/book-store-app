package com.example.bookstoreapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstoreapp.ui.screens.AddBookScreen
import com.example.bookstoreapp.ui.screens.AllBooksScreen
import com.example.bookstoreapp.ui.screens.EditBookScreen
import com.example.bookstoreapp.ui.screens.HomeScreen
import com.example.bookstoreapp.ui.screens.InventoryScreen
import com.example.bookstoreapp.ui.screens.OrderConfirmationScreen
import com.example.bookstoreapp.ui.screens.OrdersScreen
import com.example.bookstoreapp.viewmodel.BookListViewModel
import com.example.bookstoreapp.viewmodel.EditBookViewModel
import com.example.bookstoreapp.viewmodel.AddBookViewModel
import com.example.bookstoreapp.viewmodel.OrderViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.AllBooks.route) {
            AllBooksScreen(navController)
        }
        composable(Screen.Inventory.route) {
            InventoryScreen(navController)
        }
        composable(Screen.Orders.route) {
            OrdersScreen(navController)
        }
        composable(Screen.AddBook.route) {
            AddBookScreen(navController)
        }
        composable(
            route = Screen.EditBook.route,
            arguments = Screen.EditBook.arguments
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            EditBookScreen(navController, bookId)
        }
        composable(
            route = Screen.OrderConfirmation.route,
            arguments = Screen.OrderConfirmation.arguments
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
            OrderConfirmationScreen(navController, orderId)
        }
    }
} 