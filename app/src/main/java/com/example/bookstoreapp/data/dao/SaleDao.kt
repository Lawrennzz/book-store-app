package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.Sale
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getSaleById(id: Int): Sale?
    
    @Query("SELECT * FROM sales WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getSalesByDateRange(startDate: Long, endDate: Long): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE cashierUsername = :username")
    fun getSalesByCashier(username: String): Flow<List<Sale>>
    
    @Query("SELECT * FROM sales WHERE customerId = :customerId")
    fun getSalesByCustomer(customerId: Int): Flow<List<Sale>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale): Long
    
    @Update
    suspend fun updateSale(sale: Sale)
    
    @Delete
    suspend fun deleteSale(sale: Sale)
    
    @Query("SELECT SUM(finalAmount) FROM sales WHERE timestamp BETWEEN :startDate AND :endDate")
    suspend fun getTotalSalesAmount(startDate: Long, endDate: Long): Double?
} 