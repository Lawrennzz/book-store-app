package com.example.bookstoreapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryItemDao {
    @Query("SELECT * FROM inventory_items")
    fun getAllItems(): Flow<List<InventoryItem>>

    @Query("SELECT * FROM inventory_items WHERE id = :id")
    fun getItemById(id: Int): Flow<InventoryItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryItem): Long

    @Update
    suspend fun updateItem(item: InventoryItem)

    @Delete
    suspend fun deleteItem(item: InventoryItem)

    @Query("SELECT * FROM inventory_items WHERE name LIKE '%' || :searchQuery || '%' OR id = :searchQuery")
    fun searchItems(searchQuery: String): Flow<List<InventoryItem>>

    @Query("SELECT * FROM inventory_items WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<InventoryItem>>
} 