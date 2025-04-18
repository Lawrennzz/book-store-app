package com.example.bookstoreapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookstoreapp.ui.screens.*
import com.example.bookstoreapp.ui.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = if (authState == AuthViewModel.AuthState.AUTHENTICATED) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
        
        // Book screens
        composable(Screen.BookList.route) {
            BookListScreen(
                onBookClick = { bookId -> 
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onAddBook = { navController.navigate(Screen.BookAdd.route) }
            )
        }
        
        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: return@composable
            BookDetailScreen(
                bookId = bookId,
                onEdit = { navController.navigate(Screen.BookEdit.createRoute(bookId)) },
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.BookAdd.route) {
            BookEditScreen(
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.BookEdit.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: return@composable
            BookEditScreen(
                bookId = bookId,
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
        
        // Add other screen navigations (Sale, Customer, Supplier, etc.)
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    
    // Book screens
    object BookList : Screen("books")
    object BookDetail : Screen("books/{bookId}") {
        fun createRoute(bookId: Int) = "books/$bookId"
    }
    object BookAdd : Screen("books/add")
    object BookEdit : Screen("books/{bookId}/edit") {
        fun createRoute(bookId: Int) = "books/$bookId/edit"
    }
    
    // Sale screens
    object SaleList : Screen("sales")
    object SaleDetail : Screen("sales/{saleId}") {
        fun createRoute(saleId: Int) = "sales/$saleId"
    }
    object SaleCreate : Screen("sales/create")
    
    // Customer screens
    object CustomerList : Screen("customers")
    object CustomerDetail : Screen("customers/{customerId}") {
        fun createRoute(customerId: Int) = "customers/$customerId"
    }
    object CustomerAdd : Screen("customers/add")
    object CustomerEdit : Screen("customers/{customerId}/edit") {
        fun createRoute(customerId: Int) = "customers/$customerId/edit"
    }
    
    // Supplier screens
    object SupplierList : Screen("suppliers")
    object SupplierDetail : Screen("suppliers/{supplierId}") {
        fun createRoute(supplierId: Int) = "suppliers/$supplierId"
    }
    object SupplierAdd : Screen("suppliers/add")
    object SupplierEdit : Screen("suppliers/{supplierId}/edit") {
        fun createRoute(supplierId: Int) = "suppliers/$supplierId/edit"
    }
    
    // Purchase orders
    object OrderList : Screen("orders")
    object OrderDetail : Screen("orders/{orderId}") {
        fun createRoute(orderId: Int) = "orders/$orderId"
    }
    object OrderCreate : Screen("orders/create")
    
    // Reports
    object Reports : Screen("reports")
} 