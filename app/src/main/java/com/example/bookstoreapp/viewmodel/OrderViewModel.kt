package com.example.bookstoreapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.Order
import com.example.bookstoreapp.data.OrderDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

data class OrderUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderDao: OrderDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                orderDao.getAllOrders().collect { orders ->
                    _uiState.value = _uiState.value.copy(
                        orders = orders,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load orders"
                )
            }
        }
    }

    suspend fun getOrderById(orderId: String): Order {
        return orderDao.getOrderById(orderId) ?: throw Exception("Order not found")
    }

    fun createOrder(bookId: String, quantity: Int, totalPrice: Double) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val order = Order(
                    id = UUID.randomUUID().toString(),
                    bookId = bookId,
                    quantity = quantity,
                    totalPrice = totalPrice,
                    orderDate = Date(),
                    status = "Pending"
                )
                orderDao.insertOrder(order)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = "Order created successfully"
                )
                loadOrders()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to create order"
                )
            }
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val order = orderDao.getOrderById(orderId)
                if (order != null) {
                    val updatedOrder = order.copy(status = newStatus)
                    orderDao.insertOrder(updatedOrder)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = "Order status updated successfully"
                    )
                    loadOrders()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Order not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to update order status"
                )
            }
        }
    }

    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val order = orderDao.getOrderById(orderId)
                if (order != null) {
                    orderDao.deleteOrder(order.id)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        success = "Order deleted successfully"
                    )
                    loadOrders()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Order not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete order"
                )
            }
        }
    }
} 