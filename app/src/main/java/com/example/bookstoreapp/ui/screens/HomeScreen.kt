package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bookstoreapp.data.entity.UserRole
import com.example.bookstoreapp.ui.navigation.Screen
import com.example.bookstoreapp.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookstore Manager") },
                actions = {
                    if (currentUser?.role == UserRole.ADMIN) {
                        IconButton(
                            onClick = { onNavigate(Screen.UserManagement.route) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ManageAccounts,
                                contentDescription = "User Management"
                            )
                        }
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome, ${currentUser?.fullName ?: "User"}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            if (currentUser?.role == UserRole.ADMIN) {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "You have pending user approvals",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        FilledTonalButton(
                            onClick = { onNavigate(Screen.UserManagement.route) }
                        ) {
                            Text("Review Users")
                        }
                    }
                }
            }
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val menuItems = getAvailableMenuItems(authViewModel)
                items(menuItems) { item ->
                    MenuCard(
                        icon = item.icon,
                        title = item.title,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }
        }
    }
}

private fun getAvailableMenuItems(authViewModel: AuthViewModel): List<MenuItem> {
    val allItems = listOf(
        MenuItem(
            icon = Icons.AutoMirrored.Filled.MenuBook,
            title = "Books",
            route = Screen.BookList.route,
            requiredRole = UserRole.STAFF
        ),
        MenuItem(
            icon = Icons.Filled.ShoppingCart,
            title = "Sales",
            route = Screen.SaleList.route,
            requiredRole = UserRole.CASHIER
        ),
        MenuItem(
            icon = Icons.Filled.Person,
            title = "Customers",
            route = Screen.CustomerList.route,
            requiredRole = UserRole.CASHIER
        ),
        MenuItem(
            icon = Icons.Filled.Business,
            title = "Suppliers",
            route = Screen.SupplierList.route,
            requiredRole = UserRole.MANAGER
        ),
        MenuItem(
            icon = Icons.Filled.Inventory,
            title = "Purchase Orders",
            route = Screen.OrderList.route,
            requiredRole = UserRole.MANAGER
        ),
        MenuItem(
            icon = Icons.Filled.BarChart,
            title = "Reports",
            route = Screen.Reports.route,
            requiredRole = UserRole.MANAGER
        ),
        MenuItem(
            icon = Icons.Filled.ManageAccounts,
            title = "User Management",
            route = Screen.UserManagement.route,
            requiredRole = UserRole.ADMIN
        )
    )
    
    return allItems.filter { authViewModel.hasPermission(it.requiredRole) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class MenuItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val route: String,
    val requiredRole: UserRole
) 