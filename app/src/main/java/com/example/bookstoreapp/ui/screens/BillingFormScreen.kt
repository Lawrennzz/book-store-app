package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingFormScreen(
    onReview: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var couponCode by remember { mutableStateOf("") }
    var discountPercent by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Billing") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Buyer's Information",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contact,
                onValueChange = { contact = it },
                label = { Text("Contact") },
                modifier = Modifier.fillMaxWidth()
            )

            // Coupon Section
            Text(
                "Coupon code (If any):",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = couponCode,
                    onValueChange = { couponCode = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter coupon code") }
                )
                OutlinedTextField(
                    value = "-54",
                    onValueChange = { },
                    modifier = Modifier.width(100.dp),
                    enabled = false,
                    suffix = { Text("Rs") }
                )
            }

            // Discount Section
            Text(
                "Discount (If any):",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = discountPercent,
                    onValueChange = { discountPercent = it },
                    modifier = Modifier.width(100.dp),
                    suffix = { Text("%") }
                )
                OutlinedTextField(
                    value = "Rs",
                    onValueChange = { },
                    modifier = Modifier.width(100.dp),
                    enabled = false
                )
            }

            Text(
                "Note: Coupon & Discount will apply on total selling cost.",
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                label = { Text("Remarks (if any)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Add more items */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add More")
                }
                Button(
                    onClick = onReview,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Create Bill")
                }
            }
        }
    }
} 