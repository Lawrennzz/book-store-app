package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.data.entity.Book
import com.example.bookstoreapp.ui.viewmodel.BookViewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.MenuBook

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    onBookClick: (Int) -> Unit,
    onAddBook: () -> Unit,
    bookViewModel: BookViewModel = viewModel()
) {
    val books by bookViewModel.books.collectAsState()
    val searchResults by bookViewModel.searchResults.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    
    val displayedBooks = if (searchQuery.isBlank()) books else searchResults
    
    Scaffold(
        topBar = {
            if (showSearchBar) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { 
                        searchQuery = it
                        bookViewModel.searchBooks(it)
                    },
                    onSearch = { bookViewModel.searchBooks(searchQuery) },
                    active = true,
                    onActiveChange = { showSearchBar = it },
                    placeholder = { Text("Search by title, author, or ISBN") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = { 
                        IconButton(onClick = { 
                            if (searchQuery.isNotEmpty()) {
                                searchQuery = ""
                                bookViewModel.searchBooks("")
                            } else {
                                showSearchBar = false
                            }
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                ) {
                    // Search suggestions can go here
                }
            } else {
                TopAppBar(
                    title = { Text("Book Inventory") },
                    actions = {
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBook) {
                Icon(Icons.Default.Add, contentDescription = "Add Book")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (displayedBooks.isEmpty()) {
                EmptyBookList()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayedBooks) { book ->
                        BookItem(
                            book = book,
                            onClick = { onBookClick(book.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: Book,
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
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "By ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Genre: ${book.genre}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${book.price}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                val stockColor = if (book.quantity <= book.threshold) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
                Text(
                    text = "Stock: ${book.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = stockColor
                )
            }
        }
    }
}

@Composable
fun EmptyBookList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No books in inventory",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add books to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
} 