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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.data.entity.Book
import com.example.bookstoreapp.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookEditScreen(
    bookId: Int? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    bookViewModel: BookViewModel = viewModel()
) {
    val isEditMode = bookId != null
    val currentBook by bookViewModel.currentBook.collectAsState()
    
    LaunchedEffect(bookId) {
        if (bookId != null) {
            bookViewModel.loadBook(bookId)
        }
    }
    
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var publisher by remember { mutableStateOf("") }
    var edition by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var threshold by remember { mutableStateOf("") }
    
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Load values from currentBook when available
    LaunchedEffect(currentBook) {
        currentBook?.let { book ->
            title = book.title
            author = book.author
            isbn = book.isbn
            publisher = book.publisher
            edition = book.edition
            genre = book.genre
            price = book.price.toString()
            quantity = book.quantity.toString()
            threshold = book.threshold.toString()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Book" else "Add New Book") },
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
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Author") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = isbn,
                onValueChange = { isbn = it },
                label = { Text("ISBN") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = publisher,
                onValueChange = { publisher = it },
                label = { Text("Publisher") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = edition,
                onValueChange = { edition = it },
                label = { Text("Edition") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Genre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )
            
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            
            OutlinedTextField(
                value = threshold,
                onValueChange = { threshold = it },
                label = { Text("Low Stock Threshold") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            Button(
                onClick = {
                    if (validateInputs()) {
                        val book = createBookFromInputs(
                            bookId = bookId,
                            title = title,
                            author = author,
                            isbn = isbn,
                            publisher = publisher,
                            edition = edition,
                            genre = genre,
                            price = price,
                            quantity = quantity,
                            threshold = threshold,
                            currentBook = currentBook
                        )
                        
                        if (isEditMode) {
                            bookViewModel.updateBook(book)
                        } else {
                            bookViewModel.insertBook(book)
                        }
                        onSave()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(if (isEditMode) "Update Book" else "Add Book")
            }
            
            if (isEditMode) {
                OutlinedButton(
                    onClick = { showDeleteConfirmation = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete Book")
                }
            }
        }
        
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Book") },
                text = { Text("Are you sure you want to delete this book? This action cannot be undone.") },
                confirmButton = {
                    Button(
                        onClick = {
                            currentBook?.let { bookViewModel.deleteBook(it) }
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
}

private fun validateInputs(): Boolean {
    // TODO: Add validation logic
    return true
}

private fun createBookFromInputs(
    bookId: Int?,
    title: String,
    author: String,
    isbn: String,
    publisher: String,
    edition: String,
    genre: String,
    price: String,
    quantity: String,
    threshold: String,
    currentBook: Book?
): Book {
    return Book(
        id = bookId ?: 0,
        title = title,
        author = author,
        isbn = isbn,
        publisher = publisher,
        edition = edition,
        genre = genre,
        price = price.toDoubleOrNull() ?: 0.0,
        quantity = quantity.toIntOrNull() ?: 0,
        threshold = threshold.toIntOrNull() ?: 5,
        coverImageUri = currentBook?.coverImageUri ?: "",
        dateAdded = currentBook?.dateAdded ?: System.currentTimeMillis()
    )
} 