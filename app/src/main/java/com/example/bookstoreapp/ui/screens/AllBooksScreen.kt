package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.viewmodel.BookListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBooksScreen(
    navController: NavController,
    viewModel: BookListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Books") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_book") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.books) { book ->
                    BookItem(
                        book = book,
                        onEditClick = { navController.navigate("edit_book/${book.id}") },
                        onDeleteClick = { viewModel.deleteBook(book.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookItem(
    book: Book,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "by ${book.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Quantity: ${book.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Price: $${book.price}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Category: ${book.category}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

// Sample data class and data
data class Book(
    val id: String,
    val title: String,
    val author: String,
    val stockQuantity: Int,
    val price: Double
)

private val sampleBooks = listOf(
    Book("1", "The Design of Everyday Things", "Don Norman", 2, 599.0),
    Book("2", "Clean Code", "Robert C. Martin", 3, 799.0),
    Book("3", "Atomic Habits", "James Clear", 15, 499.0),
    Book("4", "Deep Work", "Cal Newport", 8, 399.0),
    Book("5", "The Psychology of Money", "Morgan Housel", 0, 299.0)
) 