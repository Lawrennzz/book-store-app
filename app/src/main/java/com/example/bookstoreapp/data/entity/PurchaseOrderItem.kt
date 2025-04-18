package com.example.bookstoreapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "purchase_order_items",
    primaryKeys = ["orderId", "bookId"],
    foreignKeys = [
        ForeignKey(
            entity = PurchaseOrder::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("bookId")
    ]
)
data class PurchaseOrderItem(
    val orderId: Int,
    val bookId: Int,
    val quantity: Int,
    val pricePerUnit: Double,
    val receivedQuantity: Int = 0
) 