package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.ui.viewmodel.AuthViewModel
import com.example.bookstoreapp.ui.viewmodel.BookViewModel
import com.example.bookstoreapp.ui.viewmodel.CustomerViewModel
import com.example.bookstoreapp.ui.viewmodel.SaleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleCreateScreen(
    onSaleComplete: () -> Unit,
    onCancel: () -> Unit,
    saleViewModel: SaleViewModel = viewModel(),
    bookViewModel: BookViewModel = viewModel(),
    customerViewModel: CustomerViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val cartItems by saleViewModel.cartItems.collectAsState()
    val cartTotal by saleViewModel.cartTotal.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    var showBookSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by bookViewModel.searchResults.collectAsState()
    
    var showCustomerSearch by remember { mutableStateOf(false) }
    var selectedCustomerId by remember { mutableStateOf<Int?>(null) }
    
    var showPaymentDialog by remember { mutableStateOf(false) }
    var discount by remember { mutableStateOf("0.00") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Sale") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (cartItems.isNotEmpty()) {
                            // Show confirmation dialog before canceling
                            // For simplicity, we're not implementing this dialog
                        } else {
                            onCancel()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (showBookSearch) {
                        IconButton(onClick = { showBookSearch = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Search")
                        }
                    } else {
                        IconButton(onClick = { showBookSearch = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search Books")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showBookSearch) {
                BookSearchSection(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { 
                        searchQuery = it
                        bookViewModel.searchBooks(it)
                    },
                    searchResults = searchResults,
                    onBookSelect = { bookId ->
                        saleViewModel.addToCart(bookId)
                        showBookSearch = false
                        searchQuery = ""
                    }
                )
            } else {
                // Main Sale Creation Content
                Box(modifier = Modifier.weight(1f)) {
                    if (cartItems.isEmpty()) {
                        EmptyCartMessage()
                    } else {
                        CartItemsList(
                            cartItems = cartItems,
                            onUpdateQuantity = { bookId, quantity ->
                                saleViewModel.updateCartItemQuantity(bookId, quantity)
                            },
                            onRemoveItem = { bookId ->
                                saleViewModel.removeFromCart(bookId)
                            }
                        )
                    }
                }
                
                // Customer and Payment Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp)
                ) {
                    // Customer selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Customer:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        OutlinedButton(
                            onClick = { showCustomerSearch = true }
                        ) {
                            Text(
                                text = if (selectedCustomerId != null) "Customer #$selectedCustomerId" else "Select Customer"
                            )
                        }
                        
                        if (selectedCustomerId != null) {
                            IconButton(onClick = { selectedCustomerId = null }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Customer")
                            }
                        }
                    }
                    
                    // Totals section
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Text(
                            text = "$${String.format("%.2f", cartTotal)}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    
                    // Discount field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Discount:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        
                        OutlinedTextField(
                            value = discount,
                            onValueChange = { discount = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            prefix = { Text("$") },
                            singleLine = true
                        )
                    }
                    
                    // Final total
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Final Total:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        val discountValue = discount.toDoubleOrNull() ?: 0.0
                        val finalTotal = (cartTotal - discountValue).coerceAtLeast(0.0)
                        
                        Text(
                            text = "$${String.format("%.2f", finalTotal)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // Payment button
                    Button(
                        onClick = { showPaymentDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = cartItems.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Process Payment")
                    }
                }
            }
        }
        
        // Customer search dialog
        if (showCustomerSearch) {
            CustomerSearchDialog(
                onDismiss = { showCustomerSearch = false },
                onCustomerSelected = { customerId ->
                    selectedCustomerId = customerId
                    showCustomerSearch = false
                },
                customerViewModel = customerViewModel
            )
        }
        
        // Payment dialog
        if (showPaymentDialog) {
            PaymentDialog(
                totalAmount = cartTotal,
                discount = discount.toDoubleOrNull() ?: 0.0,
                onDismiss = { showPaymentDialog = false },
                onPaymentComplete = { paymentMethod ->
                    val discountValue = discount.toDoubleOrNull() ?: 0.0
                    saleViewModel.completeSale(
                        cashierUsername = currentUser?.username ?: "unknown",
                        paymentMethod = paymentMethod,
                        customerId = selectedCustomerId,
                        discount = discountValue
                    )
                    onSaleComplete()
                }
            )
        }
    }
}

@Composable
fun BookSearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    searchResults: List<com.example.bookstoreapp.data.entity.Book>,
    onBookSelect: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search books by title, author, or ISBN") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
                item {
                    Text(
                        text = "No books found",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(searchResults) { book ->
                    SearchResultItem(book = book, onClick = { onBookSelect(book.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultItem(
    book: com.example.bookstoreapp.data.entity.Book,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "By ${book.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "ISBN: ${book.isbn}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format("%.2f", book.price)}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Stock: ${book.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (book.quantity <= book.threshold) 
                        MaterialTheme.colorScheme.error
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CartItemsList(
    cartItems: List<SaleViewModel.CartItem>,
    onUpdateQuantity: (Int, Int) -> Unit,
    onRemoveItem: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Shopping Cart",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cartItems) { item ->
                CartItemRow(
                    item = item,
                    onUpdateQuantity = onUpdateQuantity,
                    onRemove = onRemoveItem
                )
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: SaleViewModel.CartItem,
    onUpdateQuantity: (Int, Int) -> Unit,
    onRemove: (Int) -> Unit
) {
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
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                
                IconButton(onClick = { onRemove(item.bookId) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${String.format("%.2f", item.price)} each",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                IconButton(
                    onClick = { onUpdateQuantity(item.bookId, item.quantity - 1) },
                    enabled = item.quantity > 1
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease"
                    )
                }
                
                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                IconButton(
                    onClick = { onUpdateQuantity(item.bookId, item.quantity + 1) },
                    enabled = item.quantity < item.maxQuantity
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase"
                    )
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Subtotal: $${String.format("%.2f", item.subtotal)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun EmptyCartMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Your Cart is Empty",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Search and add books to get started",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CustomerSearchDialog(
    onDismiss: () -> Unit,
    onCustomerSelected: (Int) -> Unit,
    customerViewModel: CustomerViewModel
) {
    val customers by customerViewModel.customers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Customer") },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        customerViewModel.searchCustomers(it)
                    },
                    placeholder = { Text("Search customers") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(
                    modifier = Modifier.height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(customers) { customer ->
                        CustomerListItem(
                            customer = customer,
                            onClick = { onCustomerSelected(customer.id) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        dismissButton = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListItem(
    customer: com.example.bookstoreapp.data.entity.Customer,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = customer.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Phone: ${customer.phone}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Membership: ${customer.membershipLevel} (${customer.loyaltyPoints} points)",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PaymentDialog(
    totalAmount: Double,
    discount: Double,
    onDismiss: () -> Unit,
    onPaymentComplete: (String) -> Unit
) {
    val finalAmount = (totalAmount - discount).coerceAtLeast(0.0)
    var selectedPaymentMethod by remember { mutableStateOf("Cash") }
    val paymentMethods = listOf("Cash", "Credit Card", "Debit Card", "Mobile Payment")
    
    var amountTendered by remember { mutableStateOf("") }
    val amountTenderedDouble = amountTendered.toDoubleOrNull() ?: 0.0
    val change = (amountTenderedDouble - finalAmount).coerceAtLeast(0.0)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Process Payment") },
        text = {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Total: $${String.format("%.2f", totalAmount)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                if (discount > 0) {
                    Text(
                        text = "Discount: -$${String.format("%.2f", discount)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    Text(
                        text = "Final Amount: $${String.format("%.2f", finalAmount)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                paymentMethods.forEach { method ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPaymentMethod == method,
                            onClick = { selectedPaymentMethod = method }
                        )
                        Text(text = method)
                    }
                }
                
                if (selectedPaymentMethod == "Cash") {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = amountTendered,
                        onValueChange = { amountTendered = it },
                        label = { Text("Amount Tendered") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        prefix = { Text("$") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Change: $${String.format("%.2f", change)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onPaymentComplete(selectedPaymentMethod) },
                enabled = selectedPaymentMethod != "Cash" || amountTenderedDouble >= finalAmount
            ) {
                Text("Complete Sale")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 