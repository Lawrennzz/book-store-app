package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.PurchaseOrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderItemDao {
    @Query("SELECT * FROM purchase_order_items WHERE orderId = :orderId")
    fun getItemsForOrder(orderId: Int): Flow<List<PurchaseOrderItem>>
    
    @Query("SELECT * FROM purchase_order_items WHERE bookId = :bookId")
    fun getOrderItemsForBook(bookId: Int): Flow<List<PurchaseOrderItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: PurchaseOrderItem)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<PurchaseOrderItem>)
    
    @Update
    suspend fun updateOrderItem(orderItem: PurchaseOrderItem)
    
    @Query("UPDATE purchase_order_items SET receivedQuantity = :receivedQuantity WHERE orderId = :orderId AND bookId = :bookId")
    suspend fun updateReceivedQuantity(orderId: Int, bookId: Int, receivedQuantity: Int)
    
    @Delete
    suspend fun deleteOrderItem(orderItem: PurchaseOrderItem)
} 