package com.example.bookstoreapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.entity.Book
import com.example.bookstoreapp.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()
    
    private val _lowStockBooks = MutableStateFlow<List<Book>>(emptyList())
    val lowStockBooks: StateFlow<List<Book>> = _lowStockBooks.asStateFlow()
    
    private val _currentBook = MutableStateFlow<Book?>(null)
    val currentBook: StateFlow<Book?> = _currentBook.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults.asStateFlow()
    
    init {
        viewModelScope.launch {
            repository.allBooks.collect { books ->
                _books.value = books
            }
        }
        
        viewModelScope.launch {
            repository.lowStockBooks.collect { books ->
                _lowStockBooks.value = books
            }
        }
    }
    
    fun loadBook(id: Int) {
        viewModelScope.launch {
            _currentBook.value = repository.getBookById(id)
        }
    }
    
    fun insertBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
        }
    }
    
    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.updateBook(book)
        }
    }
    
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }
    
    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = emptyList()
            } else {
                repository.searchBooks(query).collect { books ->
                    _searchResults.value = books
                }
            }
        }
    }
    
    fun filterByGenre(genre: String) {
        viewModelScope.launch {
            repository.getBooksByGenre(genre).collect { books ->
                _searchResults.value = books
            }
        }
    }
    
    class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BookViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 