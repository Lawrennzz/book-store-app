package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.ui.viewmodel.CustomerViewModel
import com.example.bookstoreapp.ui.viewmodel.SaleViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    customerId: Int,
    onEdit: () -> Unit,
    onBack: () -> Unit,
    customerViewModel: CustomerViewModel = viewModel(),
    saleViewModel: SaleViewModel = viewModel()
) {
    val customer by customerViewModel.currentCustomer.collectAsState()
    var showMembershipDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(customerId) {
        customerViewModel.loadCustomer(customerId)
        saleViewModel.getSalesByCustomer(customerId)
    }
    
    val customerSales by saleViewModel.sales.collectAsState()
    val totalSpent = customerSales.sumOf { it.finalAmount }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customer Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (customer == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Customer profile header
                Card(
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
                                text = customer?.name ?: "",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            
                            val membershipColor = when(customer?.membershipLevel) {
                                "GOLD" -> Color(0xFFFFD700)
                                "SILVER" -> Color(0xFFC0C0C0)
                                else -> MaterialTheme.colorScheme.primary
                            }
                            
                            Surface(
                                onClick = { showMembershipDialog = true },
                                color = membershipColor.copy(alpha = 0.2f),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = membershipColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = customer?.membershipLevel ?: "STANDARD",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = membershipColor
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        DetailSection(
                            icon = Icons.Default.Phone,
                            label = "Phone",
                            value = customer?.phone ?: ""
                        )
                        
                        DetailSection(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = customer?.email ?: ""
                        )
                        
                        DetailSection(
                            icon = Icons.Default.Home,
                            label = "Address",
                            value = customer?.address ?: ""
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Loyalty Points",
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(
                                    text = "${customer?.loyaltyPoints} points",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                        
                        if (customer?.dateJoined != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                            Text(
                                text = "Customer since: ${dateFormat.format(Date(customer?.dateJoined ?: 0))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Purchase history section
                Text(
                    text = "Purchase History",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (customerSales.isEmpty()) {
                            Text(
                                text = "No purchases yet",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Total Purchases",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${customerSales.size}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Total Spent",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", totalSpent)}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Recent Transactions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Show just the most recent 3 purchases
                            customerSales.sortedByDescending { it.timestamp }.take(3).forEach { sale ->
                                RecentPurchaseItem(sale = sale)
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                            
                            if (customerSales.size > 3) {
                                TextButton(
                                    onClick = { /* Navigate to full history */ },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("View All Transactions")
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Actions section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* Create a new sale for this customer */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New Sale")
                    }
                    
                    Button(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit")
                    }
                }
            }
        }
    }
    
    if (showMembershipDialog && customer != null) {
        MembershipLevelDialog(
            currentLevel = customer?.membershipLevel ?: "STANDARD",
            onDismiss = { showMembershipDialog = false },
            onConfirm = { newLevel ->
                customerViewModel.updateMembershipLevel(customerId, newLevel)
                showMembershipDialog = false
            }
        )
    }
}

@Composable
fun DetailSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun RecentPurchaseItem(sale: com.example.bookstoreapp.data.entity.Sale) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Sale #${sale.id}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = dateFormat.format(Date(sale.timestamp)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = "$${String.format("%.2f", sale.finalAmount)}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MembershipLevelDialog(
    currentLevel: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val levels = listOf("STANDARD", "SILVER", "GOLD", "PLATINUM")
    var selectedLevel by remember { mutableStateOf(currentLevel) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Membership Level") },
        text = {
            Column {
                levels.forEach { level ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLevel == level,
                            onClick = { selectedLevel = level }
                        )
                        
                        val levelColor = when(level) {
                            "GOLD" -> Color(0xFFFFD700)
                            "SILVER" -> Color(0xFFC0C0C0)
                            "PLATINUM" -> Color(0xFFE5E4E2)
                            else -> MaterialTheme.colorScheme.primary
                        }
                        
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = levelColor,
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(text = level)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedLevel) }) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 