package com.example.bookstoreapp.data.repository

import com.example.bookstoreapp.data.dao.CustomerDao
import com.example.bookstoreapp.data.entity.Customer
import kotlinx.coroutines.flow.Flow

class CustomerRepository(private val customerDao: CustomerDao) {
    val allCustomers: Flow<List<Customer>> = customerDao.getAllCustomers()
    
    suspend fun getCustomerById(id: Int): Customer? {
        return customerDao.getCustomerById(id)
    }
    
    suspend fun insertCustomer(customer: Customer): Long {
        return customerDao.insertCustomer(customer)
    }
    
    suspend fun updateCustomer(customer: Customer) {
        customerDao.updateCustomer(customer)
    }
    
    suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }
    
    fun searchCustomers(query: String): Flow<List<Customer>> {
        return customerDao.searchCustomers("%$query%")
    }
    
    suspend fun addLoyaltyPoints(customerId: Int, points: Int) {
        customerDao.addLoyaltyPoints(customerId, points)
    }
    
    suspend fun updateMembershipLevel(customerId: Int, level: String) {
        customerDao.updateMembershipLevel(customerId, level)
    }
} 