package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<Customer>>
    
    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Int): Customer?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long
    
    @Update
    suspend fun updateCustomer(customer: Customer)
    
    @Delete
    suspend fun deleteCustomer(customer: Customer)
    
    @Query("SELECT * FROM customers WHERE name LIKE :searchQuery OR phone LIKE :searchQuery OR email LIKE :searchQuery")
    fun searchCustomers(searchQuery: String): Flow<List<Customer>>
    
    @Query("UPDATE customers SET loyaltyPoints = loyaltyPoints + :points WHERE id = :customerId")
    suspend fun addLoyaltyPoints(customerId: Int, points: Int)
    
    @Query("UPDATE customers SET membershipLevel = :level WHERE id = :customerId")
    suspend fun updateMembershipLevel(customerId: Int, level: String)
} 