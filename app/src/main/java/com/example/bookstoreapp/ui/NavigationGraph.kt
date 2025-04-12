package com.example.bookstoreapp.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookstoreapp.navigation.Screen
import com.example.bookstoreapp.ui.screens.*
import com.example.bookstoreapp.viewmodel.*

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            val viewModel = hiltViewModel<BookListViewModel>()
            HomeScreen(navController, viewModel)
        }
        composable(Screen.AllBooks.route) {
            val viewModel = hiltViewModel<BookListViewModel>()
            AllBooksScreen(navController, viewModel)
        }
        composable(Screen.Inventory.route) {
            val viewModel = hiltViewModel<BookListViewModel>()
            InventoryScreen(navController, viewModel)
        }
        composable(Screen.Orders.route) {
            val viewModel = hiltViewModel<OrderViewModel>()
            OrderScreen(navController, viewModel)
        }
        composable(Screen.AddBook.route) {
            val viewModel = hiltViewModel<AddBookViewModel>()
            AddBookScreen(navController, viewModel)
        }
        composable(
            route = Screen.EditBook.route + "/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            val viewModel = hiltViewModel<EditBookViewModel>()
            if (bookId != null) {
                viewModel.setBookId(bookId)
            }
            EditBookScreen(navController, viewModel)
        }
        composable(
            route = Screen.OrderConfirmation.route + "/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            val viewModel = hiltViewModel<OrderViewModel>()
            OrderConfirmationScreen(navController, orderId, viewModel)
        }
    }
}