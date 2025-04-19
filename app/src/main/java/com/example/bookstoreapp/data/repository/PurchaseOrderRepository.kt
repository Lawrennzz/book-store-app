package com.example.bookstoreapp.data.repository

import com.example.bookstoreapp.data.dao.BookDao
import com.example.bookstoreapp.data.dao.PurchaseOrderDao
import com.example.bookstoreapp.data.dao.PurchaseOrderItemDao
import com.example.bookstoreapp.data.entity.OrderStatus
import com.example.bookstoreapp.data.entity.PurchaseOrder
import com.example.bookstoreapp.data.entity.PurchaseOrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PurchaseOrderRepository(
    private val purchaseOrderDao: PurchaseOrderDao,
    private val purchaseOrderItemDao: PurchaseOrderItemDao,
    private val bookDao: BookDao
) {
    val allPurchaseOrders: Flow<List<PurchaseOrder>> = purchaseOrderDao.getAllPurchaseOrders()
    
    suspend fun getPurchaseOrderById(id: Int): PurchaseOrder? {
        return purchaseOrderDao.getPurchaseOrderById(id)
    }
    
    fun getPurchaseOrdersBySupplier(supplierId: Int): Flow<List<PurchaseOrder>> {
        return purchaseOrderDao.getPurchaseOrdersBySupplier(supplierId)
    }
    
    fun getPurchaseOrdersByStatus(status: OrderStatus): Flow<List<PurchaseOrder>> {
        return purchaseOrderDao.getPurchaseOrdersByStatus(status)
    }
    
    fun getPurchaseOrdersByUser(username: String): Flow<List<PurchaseOrder>> {
        return purchaseOrderDao.getPurchaseOrdersByUser(username)
    }
    
    fun getItemsForOrder(orderId: Int): Flow<List<PurchaseOrderItem>> {
        return purchaseOrderItemDao.getItemsForOrder(orderId)
    }
    
    suspend fun createOrderWithItems(order: PurchaseOrder, items: List<PurchaseOrderItem>): Long {
        return withContext(Dispatchers.IO) {
            val orderId = purchaseOrderDao.insertPurchaseOrder(order)
            
            items.forEach { item ->
                val orderItem = item.copy(orderId = orderId.toInt())
                purchaseOrderItemDao.insertOrderItem(orderItem)
            }
            
            orderId
        }
    }
    
    suspend fun updatePurchaseOrder(order: PurchaseOrder) {
        purchaseOrderDao.updatePurchaseOrder(order)
    }
    
    suspend fun updateOrderStatus(orderId: Int, status: OrderStatus) {
        purchaseOrderDao.updatePurchaseOrderStatus(orderId, status)
    }
    
    suspend fun receiveOrderItems(orderId: Int, receivedItems: Map<Int, Int>) {
        withContext(Dispatchers.IO) {
            receivedItems.forEach { (bookId, quantity) ->
                purchaseOrderItemDao.updateReceivedQuantity(orderId, bookId, quantity)
                bookDao.increaseStock(bookId, quantity)
            }
            
            // Update order status based on all items being received
            // TODO: Implement logic to check if all items are received and update order status
            // For future implementation: Use orderItems to verify receipt status
            // val orderItems = purchaseOrderItemDao.getItemsForOrder(orderId)
        }
    }
    
    suspend fun deletePurchaseOrder(order: PurchaseOrder) {
        purchaseOrderDao.deletePurchaseOrder(order)
    }
} 