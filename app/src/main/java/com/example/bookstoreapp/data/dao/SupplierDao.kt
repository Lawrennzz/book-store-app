package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.Supplier
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {
    @Query("SELECT * FROM suppliers ORDER BY name ASC")
    fun getAllSuppliers(): Flow<List<Supplier>>
    
    @Query("SELECT * FROM suppliers WHERE id = :id")
    suspend fun getSupplierById(id: Int): Supplier?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: Supplier): Long
    
    @Update
    suspend fun updateSupplier(supplier: Supplier)
    
    @Delete
    suspend fun deleteSupplier(supplier: Supplier)
    
    @Query("SELECT * FROM suppliers WHERE name LIKE :searchQuery OR contactPerson LIKE :searchQuery")
    fun searchSuppliers(searchQuery: String): Flow<List<Supplier>>
} 