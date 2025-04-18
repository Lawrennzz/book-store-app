package com.example.bookstoreapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.entity.Customer
import com.example.bookstoreapp.data.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {
    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()
    
    private val _currentCustomer = MutableStateFlow<Customer?>(null)
    val currentCustomer: StateFlow<Customer?> = _currentCustomer.asStateFlow()
    
    init {
        viewModelScope.launch {
            repository.allCustomers.collect { customers ->
                _customers.value = customers
            }
        }
    }
    
    fun loadCustomer(id: Int) {
        viewModelScope.launch {
            _currentCustomer.value = repository.getCustomerById(id)
        }
    }
    
    fun insertCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.insertCustomer(customer)
        }
    }
    
    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.updateCustomer(customer)
        }
    }
    
    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            repository.deleteCustomer(customer)
        }
    }
    
    fun searchCustomers(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                repository.allCustomers.collect { customers ->
                    _customers.value = customers
                }
            } else {
                repository.searchCustomers(query).collect { customers ->
                    _customers.value = customers
                }
            }
        }
    }
    
    fun updateMembershipLevel(customerId: Int, level: String) {
        viewModelScope.launch {
            repository.updateMembershipLevel(customerId, level)
            loadCustomer(customerId) // Reload customer data
        }
    }
    
    class CustomerViewModelFactory(private val repository: CustomerRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CustomerViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 