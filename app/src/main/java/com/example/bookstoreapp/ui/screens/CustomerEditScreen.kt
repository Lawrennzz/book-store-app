package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.data.entity.Customer
import com.example.bookstoreapp.ui.viewmodel.CustomerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerEditScreen(
    customerId: Int? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    customerViewModel: CustomerViewModel = viewModel()
) {
    val isEditMode = customerId != null
    val currentCustomer by customerViewModel.currentCustomer.collectAsState()
    
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    
    LaunchedEffect(customerId) {
        if (customerId != null) {
            customerViewModel.loadCustomer(customerId)
        }
    }
    
    // Load values from currentCustomer when available
    LaunchedEffect(currentCustomer) {
        if (currentCustomer != null) {
            name = currentCustomer!!.name
            phone = currentCustomer!!.phone
            email = currentCustomer!!.email
            address = currentCustomer!!.address
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Customer" else "Add Customer") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                minLines = 2
            )
            
            Button(
                onClick = {
                    // Validate input
                    if (name.isBlank() || phone.isBlank()) {
                        // Show error
                        return@Button
                    }
                    
                    if (isEditMode && currentCustomer != null) {
                        // Update existing customer
                        customerViewModel.updateCustomer(
                            currentCustomer!!.copy(
                                name = name,
                                phone = phone,
                                email = email,
                                address = address
                            )
                        )
                    } else {
                        // Create new customer
                        customerViewModel.insertCustomer(
                            Customer(
                                name = name,
                                phone = phone,
                                email = email,
                                address = address,
                                membershipLevel = "STANDARD",
                                loyaltyPoints = 0,
                                dateJoined = System.currentTimeMillis()
                            )
                        )
                    }
                    
                    onSave()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditMode) "Update Customer" else "Add Customer")
            }
            
            if (isEditMode) {
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = { showDeleteConfirmation = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Customer")
                }
            }
        }
    }
    
    if (showDeleteConfirmation && currentCustomer != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Customer") },
            text = { 
                Text("Are you sure you want to delete ${currentCustomer!!.name}? This action cannot be undone.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        customerViewModel.deleteCustomer(currentCustomer!!)
                        showDeleteConfirmation = false
                        onCancel() // Navigate back
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
} 