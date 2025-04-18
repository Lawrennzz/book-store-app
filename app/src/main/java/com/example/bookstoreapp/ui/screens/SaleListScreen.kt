package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.data.entity.Sale
import com.example.bookstoreapp.ui.viewmodel.SaleViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleListScreen(
    onSaleClick: (Int) -> Unit,
    onCreateSale: () -> Unit,
    saleViewModel: SaleViewModel = viewModel()
) {
    val sales by saleViewModel.sales.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sales History") },
                actions = {
                    IconButton(onClick = { /* TODO: Implement filtering */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateSale) {
                Icon(Icons.Default.Add, contentDescription = "New Sale")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (sales.isEmpty()) {
                EmptySaleList()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sales) { sale ->
                        SaleItem(
                            sale = sale,
                            onClick = { onSaleClick(sale.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleItem(
    sale: Sale,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sale #${sale.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                
                val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                Text(
                    text = dateFormat.format(Date(sale.timestamp)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Cashier: ${sale.cashierUsername}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = "Payment: ${sale.paymentMethod}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    if (sale.discount > 0) {
                        Text(
                            text = "Total: $${String.format("%.2f", sale.totalAmount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Text(
                            text = "Discount: -$${String.format("%.2f", sale.discount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    Text(
                        text = "Final: $${String.format("%.2f", sale.finalAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun EmptySaleList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No Sales Records",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Create a new sale to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
} 