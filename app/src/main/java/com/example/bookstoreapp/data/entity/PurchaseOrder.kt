package com.example.bookstoreapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class OrderStatus {
    DRAFT, PLACED, RECEIVED, PARTIAL, CANCELLED
}

@Entity(
    tableName = "purchase_orders",
    foreignKeys = [
        ForeignKey(
            entity = Supplier::class,
            parentColumns = ["id"],
            childColumns = ["supplierId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["createdBy"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("supplierId"),
        Index("createdBy")
    ]
)
data class PurchaseOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val supplierId: Int,
    val orderDate: Long = System.currentTimeMillis(),
    val expectedDeliveryDate: Long? = null,
    val status: OrderStatus = OrderStatus.DRAFT,
    val totalAmount: Double,
    val notes: String = "",
    val createdBy: String
) 