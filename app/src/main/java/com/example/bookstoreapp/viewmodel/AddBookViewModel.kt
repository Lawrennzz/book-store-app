package com.example.bookstoreapp.viewmodel

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

data class AddBookUiState(
    val title: String = "",
    val author: String = "",
    val quantity: String = "",
    val price: String = "",
    val category: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

class AddBookViewModel(
    private val bookDao: BookDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddBookUiState())
    val uiState: StateFlow<AddBookUiState> = _uiState.asStateFlow()
    
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
    
    fun saveBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val currentState = _uiState.value
                
                // Validate fields
                val errors = mutableMapOf<String, String>()
                
                if (currentState.title.isBlank()) {
                    errors["title"] = "Title is required"
                }
                
                if (currentState.author.isBlank()) {
                    errors["author"] = "Author is required"
                }
                
                if (currentState.quantity.isBlank()) {
                    errors["quantity"] = "Quantity is required"
                } else {
                    try {
                        val quantity = currentState.quantity.toInt()
                        if (quantity < 0) {
                            errors["quantity"] = "Quantity cannot be negative"
                        }
                    } catch (e: NumberFormatException) {
                        errors["quantity"] = "Invalid quantity format"
                    }
                }
                
                if (currentState.price.isBlank()) {
                    errors["price"] = "Price is required"
                } else {
                    try {
                        val price = currentState.price.toDouble()
                        if (price < 0) {
                            errors["price"] = "Price cannot be negative"
                        }
                    } catch (e: NumberFormatException) {
                        errors["price"] = "Invalid price format"
                    }
                }
                
                if (currentState.category.isBlank()) {
                    errors["category"] = "Category is required"
                }
                
                if (errors.isNotEmpty()) {
                    _uiState.update { it.copy(fieldErrors = errors, isLoading = false) }
                    return@launch
                }
                
                val book = Book(
                    id = UUID.randomUUID().toString(),
                    title = currentState.title,
                    author = currentState.author,
                    quantity = currentState.quantity.toInt(),
                    price = currentState.price.toDouble(),
                    category = currentState.category
                )
                
                bookDao.insertBook(book)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        successMessage = "Book added successfully",
                        fieldErrors = emptyMap()
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = "Failed to add book: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
} 