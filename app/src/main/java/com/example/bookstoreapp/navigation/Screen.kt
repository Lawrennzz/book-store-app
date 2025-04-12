package com.example.bookstoreapp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AllBooks : Screen("all_books")
    object Inventory : Screen("inventory")
    object Orders : Screen("orders")
    object AddBook : Screen("add_book")
    object EditBook : Screen("edit_book") {
        val arguments = listOf(
            navArgument("bookId") {
                type = NavType.StringType
            }
        )
    }
    object OrderConfirmation : Screen("order_confirmation") {
        val arguments = listOf(
            navArgument("orderId") {
                type = NavType.StringType
            }
        )
    }
} 