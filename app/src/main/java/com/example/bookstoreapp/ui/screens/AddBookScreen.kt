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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookstoreapp.viewmodel.AddBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    navController: NavController,
    viewModel: AddBookViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            navController.navigateUp()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Book") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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