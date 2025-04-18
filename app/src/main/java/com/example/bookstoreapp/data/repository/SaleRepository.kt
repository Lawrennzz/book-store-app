package com.example.bookstoreapp.data.repository

import com.example.bookstoreapp.data.dao.BookDao
import com.example.bookstoreapp.data.dao.SaleDao
import com.example.bookstoreapp.data.dao.SaleItemDao
import com.example.bookstoreapp.data.dao.TopSellingBook
import com.example.bookstoreapp.data.entity.Sale
import com.example.bookstoreapp.data.entity.SaleItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaleRepository(
    private val saleDao: SaleDao,
    private val saleItemDao: SaleItemDao,
    private val bookDao: BookDao
) {
    val allSales: Flow<List<Sale>> = saleDao.getAllSales()
    
    suspend fun getSaleById(id: Int): Sale? {
        return saleDao.getSaleById(id)
    }
    
    fun getSalesByDateRange(startDate: Long, endDate: Long): Flow<List<Sale>> {
        return saleDao.getSalesByDateRange(startDate, endDate)
    }
    
    fun getSalesByCashier(username: String): Flow<List<Sale>> {
        return saleDao.getSalesByCashier(username)
    }
    
    fun getSalesByCustomer(customerId: Int): Flow<List<Sale>> {
        return saleDao.getSalesByCustomer(customerId)
    }
    
    fun getSaleItemsForSale(saleId: Int): Flow<List<SaleItem>> {
        return saleItemDao.getSaleItemsForSale(saleId)
    }
    
    suspend fun createSaleWithItems(sale: Sale, items: List<SaleItem>): Long {
        return withContext(Dispatchers.IO) {
            val saleId = saleDao.insertSale(sale)
            
            items.forEach { item ->
                val saleItem = item.copy(saleId = saleId.toInt())
                saleItemDao.insertSaleItem(saleItem)
                bookDao.decreaseStock(saleItem.bookId, saleItem.quantity)
            }
            
            saleId
        }
    }
    
    suspend fun updateSale(sale: Sale) {
        saleDao.updateSale(sale)
    }
    
    suspend fun deleteSale(sale: Sale) {
        saleDao.deleteSale(sale)
    }
    
    suspend fun getTotalSalesAmount(startDate: Long, endDate: Long): Double {
        return saleDao.getTotalSalesAmount(startDate, endDate) ?: 0.0
    }
    
    suspend fun getTopSellingBooks(limit: Int): Map<Int, Int> {
        val topBooks = saleItemDao.getTopSellingBooks(limit)
        return topBooks.associate { it.bookId to it.totalSold }
    }
} 