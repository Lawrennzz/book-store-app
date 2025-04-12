package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookstoreapp.viewmodel.EditBookViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(
    navController: NavController,
    viewModel: EditBookViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var showSuccessMessage by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            showSuccessMessage = true
            delay(1500) // Show success message for 1.5 seconds
            navController.navigateUp()
        }
    }

    if (uiState.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirmation() },
            title = { Text("Delete Book") },
            text = { Text("Are you sure you want to delete this book?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteBook()
                        viewModel.hideDeleteConfirmation()
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDeleteConfirmation() }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState.showDiscardConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDiscardConfirmation() },
            title = { Text("Discard Changes") },
            text = { Text("You have unsaved changes. Are you sure you want to discard them?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.hideDiscardConfirmation()
                        navController.navigateUp()
                    }
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDiscardConfirmation() }) {
                    Text("Keep Editing")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Book") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (viewModel.hasUnsavedChanges()) {
                                viewModel.showDiscardConfirmation()
                            } else {
                                navController.navigateUp()
                            }
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.showDeleteConfirmation() }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Book")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveBook() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Book")
            }
        }
    ) { padding ->
        if (showSuccessMessage) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = uiState.successMessage ?: "",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = viewModel::updateTitle,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.fieldErrors.containsKey("title")
                )
                if (uiState.fieldErrors.containsKey("title")) {
                    Text(
                        text = uiState.fieldErrors["title"] ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = uiState.author,
                    onValueChange = viewModel::updateAuthor,
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.fieldErrors.containsKey("author")
                )
                if (uiState.fieldErrors.containsKey("author")) {
                    Text(
                        text = uiState.fieldErrors["author"] ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = uiState.quantity,
                    onValueChange = viewModel::updateQuantity,
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.fieldErrors.containsKey("quantity")
                )
                if (uiState.fieldErrors.containsKey("quantity")) {
                    Text(
                        text = uiState.fieldErrors["quantity"] ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = uiState.price,
                    onValueChange = viewModel::updatePrice,
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.fieldErrors.containsKey("price")
                )
                if (uiState.fieldErrors.containsKey("price")) {
                    Text(
                        text = uiState.fieldErrors["price"] ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = uiState.category,
                    onValueChange = viewModel::updateCategory,
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.fieldErrors.containsKey("category")
                )
                if (uiState.fieldErrors.containsKey("category")) {
                    Text(
                        text = uiState.fieldErrors["category"] ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
} 