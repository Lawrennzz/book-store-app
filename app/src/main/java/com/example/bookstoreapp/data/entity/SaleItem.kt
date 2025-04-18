package com.example.bookstoreapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "sale_items",
    primaryKeys = ["saleId", "bookId"],
    foreignKeys = [
        ForeignKey(
            entity = Sale::class,
            parentColumns = ["id"],
            childColumns = ["saleId"],
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
data class SaleItem(
    val saleId: Int,
    val bookId: Int,
    val quantity: Int,
    val pricePerUnit: Double,
    val subtotal: Double
) 