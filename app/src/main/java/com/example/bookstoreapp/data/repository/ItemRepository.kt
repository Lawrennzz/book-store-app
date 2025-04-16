package com.example.bookstoreapp.data.repository

import com.example.bookstoreapp.data.dao.ItemDao
import com.example.bookstoreapp.data.entity.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun insert(item: Item) = itemDao.insert(item)
    suspend fun update(item: Item) = itemDao.update(item)
    suspend fun delete(item: Item) = itemDao.delete(item)
    fun getItemById(itemId: Int): Flow<Item> = itemDao.getItemById(itemId)
    fun searchItems(query: String): Flow<List<Item>> = itemDao.searchItems(query)
}