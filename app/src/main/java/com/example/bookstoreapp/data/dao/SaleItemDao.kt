package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.SaleItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleItemDao {
    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    fun getSaleItemsForSale(saleId: Int): Flow<List<SaleItem>>
    
    @Query("SELECT * FROM sale_items WHERE bookId = :bookId")
    fun getSaleItemsForBook(bookId: Int): Flow<List<SaleItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItem(saleItem: SaleItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleItems(saleItems: List<SaleItem>)
    
    @Update
    suspend fun updateSaleItem(saleItem: SaleItem)
    
    @Delete
    suspend fun deleteSaleItem(saleItem: SaleItem)
    
    @Query("SELECT SUM(quantity) FROM sale_items WHERE bookId = :bookId")
    suspend fun getTotalSoldQuantityForBook(bookId: Int): Int?
    
    @Query("SELECT bookId, SUM(quantity) as totalSold FROM sale_items GROUP BY bookId ORDER BY totalSold DESC LIMIT :limit")
    suspend fun getTopSellingBooks(limit: Int): List<TopSellingBook>
}

data class TopSellingBook(
    val bookId: Int,
    val totalSold: Int
) 