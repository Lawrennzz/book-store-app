package com.example.bookstoreapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.BookDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class BookListUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

class BookListViewModel(
    private val bookDao: BookDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()
    
    init {
        loadBooks()
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                bookDao.getAllBooks().collectLatest { books ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        books = books,
                        error = null
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load books"
                ) }
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isBlank()) {
            loadBooks()
        } else {
            searchBooks(query)
        }
    }
    
    private fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                bookDao.searchBooks(query).collectLatest { books ->
                    _uiState.update { it.copy(books = books, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to search books: ${e.message}", isLoading = false) }
            }
        }
    }
    
    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                bookDao.deleteBook(bookId)
                loadBooks() // Reload the list after deletion
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to delete book: ${e.message}", isLoading = false) }
            }
        }
    }
} 