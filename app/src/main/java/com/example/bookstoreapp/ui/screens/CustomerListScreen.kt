package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.data.entity.Customer
import com.example.bookstoreapp.ui.viewmodel.CustomerViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    onCustomerClick: (Int) -> Unit,
    onAddCustomer: () -> Unit,
    customerViewModel: CustomerViewModel = viewModel()
) {
    val customers by customerViewModel.customers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customers") },
                actions = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { 
                            searchQuery = ""
                            customerViewModel.searchCustomers("")
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCustomer) {
                Icon(Icons.Default.Add, contentDescription = "Add Customer")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { 
                    searchQuery = it
                    customerViewModel.searchCustomers(it)
                },
                placeholder = { Text("Search customers...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )
            
            if (customers.isEmpty()) {
                EmptyCustomerList()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(customers) { customer ->
                        CustomerItem(
                            customer = customer,
                            onClick = { onCustomerClick(customer.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerItem(
    customer: Customer,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = customer.name,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Phone: ${customer.phone}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Text(
                        text = "Email: ${customer.email}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    val membershipColor = when(customer.membershipLevel) {
                        "GOLD" -> Color(0xFFFFD700)
                        "SILVER" -> Color(0xFFC0C0C0)
                        else -> MaterialTheme.colorScheme.primary
                    }
                    
                    Surface(
                        color = membershipColor.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = customer.membershipLevel,
                            style = MaterialTheme.typography.labelMedium,
                            color = membershipColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${customer.loyaltyPoints} points",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCustomerList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Customers Found",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Add customers to start building your loyalty program",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
} 