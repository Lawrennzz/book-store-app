package com.example.bookstoreapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.entity.Sale
import com.example.bookstoreapp.data.entity.SaleItem
import com.example.bookstoreapp.data.repository.BookRepository
import com.example.bookstoreapp.data.repository.CustomerRepository
import com.example.bookstoreapp.data.repository.SaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class SaleViewModel(
    private val saleRepository: SaleRepository,
    private val bookRepository: BookRepository,
    private val customerRepository: CustomerRepository
) : ViewModel() {
    private val _sales = MutableStateFlow<List<Sale>>(emptyList())
    val sales: StateFlow<List<Sale>> = _sales.asStateFlow()
    
    private val _currentSale = MutableStateFlow<Sale?>(null)
    val currentSale: StateFlow<Sale?> = _currentSale.asStateFlow()
    
    private val _currentSaleItems = MutableStateFlow<List<SaleItem>>(emptyList())
    val currentSaleItems: StateFlow<List<SaleItem>> = _currentSaleItems.asStateFlow()
    
    // For the POS cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _cartTotal = MutableStateFlow(0.0)
    val cartTotal: StateFlow<Double> = _cartTotal.asStateFlow()
    
    init {
        viewModelScope.launch {
            saleRepository.allSales.collect { sales ->
                _sales.value = sales
            }
        }
    }
    
    fun loadSale(id: Int) {
        viewModelScope.launch {
            _currentSale.value = saleRepository.getSaleById(id)
            
            saleRepository.getSaleItemsForSale(id).collect { items ->
                _currentSaleItems.value = items
            }
        }
    }
    
    fun addToCart(bookId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            val book = bookRepository.getBookById(bookId) ?: return@launch
            
            val existingItem = _cartItems.value.find { it.bookId == bookId }
            if (existingItem != null) {
                // Update existing item
                val updatedItems = _cartItems.value.map {
                    if (it.bookId == bookId) {
                        it.copy(quantity = it.quantity + quantity, subtotal = (it.quantity + quantity) * it.price)
                    } else {
                        it
                    }
                }
                _cartItems.value = updatedItems
            } else {
                // Add new item
                val newItem = CartItem(
                    bookId = book.id,
                    title = book.title,
                    price = book.price,
                    quantity = quantity,
                    subtotal = book.price * quantity,
                    maxQuantity = book.quantity
                )
                _cartItems.value = _cartItems.value + newItem
            }
            
            updateCartTotal()
        }
    }
    
    fun updateCartItemQuantity(bookId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(bookId)
            return
        }
        
        val updatedItems = _cartItems.value.map {
            if (it.bookId == bookId) {
                it.copy(quantity = quantity, subtotal = quantity * it.price)
            } else {
                it
            }
        }
        _cartItems.value = updatedItems
        updateCartTotal()
    }
    
    fun removeFromCart(bookId: Int) {
        _cartItems.value = _cartItems.value.filter { it.bookId != bookId }
        updateCartTotal()
    }
    
    fun clearCart() {
        _cartItems.value = emptyList()
        _cartTotal.value = 0.0
    }
    
    private fun updateCartTotal() {
        _cartTotal.value = _cartItems.value.sumOf { it.subtotal }
    }
    
    fun completeSale(cashierUsername: String, paymentMethod: String, customerId: Int? = null, discount: Double = 0.0) {
        viewModelScope.launch {
            val items = _cartItems.value
            if (items.isEmpty()) return@launch
            
            val total = _cartTotal.value
            val finalAmount = total - discount
            
            val sale = Sale(
                timestamp = System.currentTimeMillis(),
                totalAmount = total,
                discount = discount,
                finalAmount = finalAmount,
                paymentMethod = paymentMethod,
                cashierUsername = cashierUsername,
                customerId = customerId
            )
            
            val saleItems = items.map { cartItem ->
                SaleItem(
                    saleId = 0, // Will be replaced with the actual sale ID
                    bookId = cartItem.bookId,
                    quantity = cartItem.quantity,
                    pricePerUnit = cartItem.price,
                    subtotal = cartItem.subtotal
                )
            }
            
            val saleId = saleRepository.createSaleWithItems(sale, saleItems)
            
            // Add loyalty points if customer exists
            if (customerId != null) {
                // Simple loyalty points calculation: 1 point per $10 spent (adjust as needed)
                val loyaltyPoints = (finalAmount / 10).toInt()
                if (loyaltyPoints > 0) {
                    customerRepository.addLoyaltyPoints(customerId, loyaltyPoints)
                }
            }
            
            clearCart()
        }
    }
    
    fun getSalesByDateRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            saleRepository.getSalesByDateRange(startDate, endDate).collect { filteredSales ->
                _sales.value = filteredSales
            }
        }
    }
    
    fun getSalesByCashier(username: String) {
        viewModelScope.launch {
            saleRepository.getSalesByCashier(username).collect { filteredSales ->
                _sales.value = filteredSales
            }
        }
    }
    
    fun getSalesByCustomer(customerId: Int) {
        viewModelScope.launch {
            saleRepository.getSalesByCustomer(customerId).collect { filteredSales ->
                _sales.value = filteredSales
            }
        }
    }
    
    data class CartItem(
        val bookId: Int,
        val title: String,
        val price: Double,
        val quantity: Int,
        val subtotal: Double,
        val maxQuantity: Int // For inventory control
    )
    
    class SaleViewModelFactory(
        private val saleRepository: SaleRepository,
        private val bookRepository: BookRepository,
        private val customerRepository: CustomerRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SaleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SaleViewModel(saleRepository, bookRepository, customerRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 