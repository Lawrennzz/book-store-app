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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookstoreapp.data.Order
import com.example.bookstoreapp.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    var showStatusDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orders") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.loadOrders() }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.orders.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No orders found",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.orders) { order ->
                            OrderCard(
                                order = order,
                                dateFormat = dateFormat,
                                onStatusClick = {
                                    selectedOrder = order
                                    showStatusDialog = true
                                },
                                onDeleteClick = {
                                    selectedOrder = order
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }

            // Status Update Dialog
            if (showStatusDialog && selectedOrder != null) {
                StatusUpdateDialog(
                    currentStatus = selectedOrder!!.status,
                    onStatusSelected = { newStatus ->
                        viewModel.updateOrderStatus(selectedOrder!!.id, newStatus)
                        showStatusDialog = false
                    },
                    onDismiss = { showStatusDialog = false }
                )
            }

            // Delete Confirmation Dialog
            if (showDeleteDialog && selectedOrder != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Delete Order") },
                    text = { Text("Are you sure you want to delete this order?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteOrder(selectedOrder!!.id)
                                showDeleteDialog = false
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Success Message Snackbar
            uiState.success?.let { message ->
                LaunchedEffect(message) {
                    SnackbarHostState().showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderCard(
    order: Order,
    dateFormat: SimpleDateFormat,
    onStatusClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.id.take(8)}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    IconButton(onClick = onStatusClick) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Update Status",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Order",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OrderDetail("Book ID", order.bookId)
            OrderDetail("Quantity", order.quantity.toString())
            OrderDetail("Total Price", "$${String.format("%.2f", order.totalPrice)}")
            OrderDetail("Date", dateFormat.format(order.orderDate))
            OrderStatusChip(order.status)
        }
    }
}

@Composable
private fun OrderDetail(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun OrderStatusChip(status: String) {
    val (color, containerColor) = when (status.lowercase()) {
        "pending" -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.primaryContainer
        "completed" -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.secondaryContainer
        "cancelled" -> MaterialTheme.colorScheme.error to MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.tertiaryContainer
    }

    Surface(
        modifier = Modifier.padding(top = 8.dp),
        color = containerColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun StatusUpdateDialog(
    currentStatus: String,
    onStatusSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val statusOptions = listOf("Pending", "Processing", "Completed", "Cancelled")
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Order Status") },
        text = {
            Column {
                statusOptions.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = status == selectedStatus,
                            onClick = { selectedStatus = status }
                        )
                        Text(
                            text = status,
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onStatusSelected(selectedStatus) }) {
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