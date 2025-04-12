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
        fun createRoute(bookId: String) = "edit_book/$bookId"
    }
    object OrderConfirmation : Screen("order_confirmation") {
        val arguments = listOf(
            navArgument("orderId") {
                type = NavType.StringType
            }
        )
        fun createRoute(orderId: String) = "order_confirmation/$orderId"
    }
    object InventoryList : Screen("inventory_list")
    object AddItem : Screen("add_item")
    object EditItem : Screen("edit_item/{itemId}") {
        fun createRoute(itemId: Int) = "edit_item/$itemId"
    }
    object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: Int) = "item_detail/$itemId"
    }
} 