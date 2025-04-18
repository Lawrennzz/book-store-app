package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.ui.viewmodel.BookViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    bookId: Int,
    onEdit: () -> Unit,
    onBack: () -> Unit,
    bookViewModel: BookViewModel = viewModel()
) {
    val book by bookViewModel.currentBook.collectAsState()
    
    LaunchedEffect(bookId) {
        bookViewModel.loadBook(bookId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
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
        if (book == null) {
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
                // Book image or placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    if (book?.coverImageUri?.isNotEmpty() == true) {
                        // Display book cover image if available
                        // Image(...)
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            tint = Color.LightGray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = book?.title ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "By ${book?.author}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                
                DetailRow(
                    label = "ISBN",
                    value = book?.isbn ?: "",
                    icon = Icons.Default.Info
                )
                
                DetailRow(
                    label = "Publisher",
                    value = book?.publisher ?: "",
                    icon = Icons.Outlined.Business
                )
                
                DetailRow(
                    label = "Edition",
                    value = book?.edition ?: "",
                    icon = Icons.Outlined.Bookmark
                )
                
                DetailRow(
                    label = "Genre",
                    value = book?.genre ?: "",
                    icon = Icons.Outlined.Category
                )
                
                DetailRow(
                    label = "Price",
                    value = "$${book?.price}",
                    icon = Icons.Outlined.AttachMoney
                )
                
                val stockStatus = when {
                    book == null -> ""
                    book!!.quantity <= 0 -> "Out of Stock"
                    book!!.quantity <= book!!.threshold -> "Low Stock (${book!!.quantity})"
                    else -> "In Stock (${book!!.quantity})"
                }
                
                val stockColor = when {
                    book == null -> Color.Gray
                    book!!.quantity <= 0 -> MaterialTheme.colorScheme.error
                    book!!.quantity <= book!!.threshold -> Color(0xFFFFA000) // Amber
                    else -> Color(0xFF4CAF50) // Green
                }
                
                DetailRow(
                    label = "Stock Status",
                    value = stockStatus,
                    icon = Icons.Outlined.Inventory,
                    valueColor = stockColor
                )
                
                book?.dateAdded?.let { timestamp ->
                    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    val dateAdded = dateFormat.format(Date(timestamp))
                    
                    DetailRow(
                        label = "Added On",
                        value = dateAdded,
                        icon = Icons.Default.DateRange
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
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
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Button(
                        onClick = { /* TODO: Adjust Stock */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Inventory,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Adjust Stock")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    valueColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = valueColor
            )
        }
    }
} 