package com.example.bookstoreapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.Book
import com.example.bookstoreapp.data.BookDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class EditBookUiState(
    val title: String = "",
    val author: String = "",
    val quantity: String = "",
    val price: String = "",
    val category: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmation: Boolean = false,
    val isDeleted: Boolean = false,
    val showDiscardConfirmation: Boolean = false,
    val successMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

class EditBookViewModel(
    private val bookDao: BookDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditBookUiState())
    val uiState: StateFlow<EditBookUiState> = _uiState.asStateFlow()
    
    private var bookId: String? = null
    
    fun setBookId(id: String) {
        bookId = id
        loadBook()
    }
    
    fun loadBook() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val book = bookId?.let { bookDao.getBookById(it) }
                if (book != null) {
                    _uiState.update {
                        it.copy(
                            title = book.title,
                            author = book.author,
                            quantity = book.quantity.toString(),
                            price = book.price.toString(),
                            category = book.category,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            error = "Book not found",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to load book",
                        isLoading = false
                    )
                }
            }
        }
    }
    
    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }
    
    fun updateAuthor(author: String) {
        _uiState.update { it.copy(author = author) }
    }
    
    fun updateQuantity(quantity: String) {
        _uiState.update { it.copy(quantity = quantity) }
    }
    
    fun updatePrice(price: String) {
        _uiState.update { it.copy(price = price) }
    }
    
    fun updateCategory(category: String) {
        _uiState.update { it.copy(category = category) }
    }
    
    fun showDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }
    
    fun hideDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }
    
    fun showDiscardConfirmation() {
        _uiState.update { it.copy(showDiscardConfirmation = true) }
    }
    
    fun hideDiscardConfirmation() {
        _uiState.update { it.copy(showDiscardConfirmation = false) }
    }
    
    fun hasUnsavedChanges(): Boolean {
        val state = _uiState.value
        return state.title.isNotBlank() ||
                state.author.isNotBlank() ||
                state.quantity.isNotBlank() ||
                state.price.isNotBlank() ||
                state.category.isNotBlank()
    }
    
    fun saveBook() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val book = Book(
                    id = bookId ?: "",
                    title = _uiState.value.title,
                    author = _uiState.value.author,
                    quantity = _uiState.value.quantity.toIntOrNull() ?: 0,
                    price = _uiState.value.price.toDoubleOrNull() ?: 0.0,
                    category = _uiState.value.category
                )
                bookDao.updateBook(book)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Book updated successfully"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to save book",
                        isLoading = false
                    )
                }
            }
        }
    }
    
    fun deleteBook() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                bookId?.let { bookDao.deleteBook(it) }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Book deleted successfully"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to delete book",
                        isLoading = false
                    )
                }
            }
        }
    }
} 