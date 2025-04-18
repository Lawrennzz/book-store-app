package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.OrderStatus
import com.example.bookstoreapp.data.entity.PurchaseOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderDao {
    @Query("SELECT * FROM purchase_orders ORDER BY orderDate DESC")
    fun getAllPurchaseOrders(): Flow<List<PurchaseOrder>>
    
    @Query("SELECT * FROM purchase_orders WHERE id = :id")
    suspend fun getPurchaseOrderById(id: Int): PurchaseOrder?
    
    @Query("SELECT * FROM purchase_orders WHERE supplierId = :supplierId")
    fun getPurchaseOrdersBySupplier(supplierId: Int): Flow<List<PurchaseOrder>>
    
    @Query("SELECT * FROM purchase_orders WHERE status = :status")
    fun getPurchaseOrdersByStatus(status: OrderStatus): Flow<List<PurchaseOrder>>
    
    @Query("SELECT * FROM purchase_orders WHERE createdBy = :username")
    fun getPurchaseOrdersByUser(username: String): Flow<List<PurchaseOrder>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchaseOrder(purchaseOrder: PurchaseOrder): Long
    
    @Update
    suspend fun updatePurchaseOrder(purchaseOrder: PurchaseOrder)
    
    @Query("UPDATE purchase_orders SET status = :status WHERE id = :orderId")
    suspend fun updatePurchaseOrderStatus(orderId: Int, status: OrderStatus)
    
    @Delete
    suspend fun deletePurchaseOrder(purchaseOrder: PurchaseOrder)
} 