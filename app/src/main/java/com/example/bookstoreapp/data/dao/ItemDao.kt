package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :itemId")
    fun getItemById(itemId: Int): Flow<Item>

    @Query("SELECT * FROM items WHERE name LIKE '%' || :query || '%' OR id LIKE '%' || :query || '%'")
    fun searchItems(query: String): Flow<List<Item>>
}