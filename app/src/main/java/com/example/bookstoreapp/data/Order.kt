package com.example.bookstoreapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val id: String,
    val bookId: String,
    val quantity: Int,
    val totalPrice: Double,
    val orderDate: Date,
    val status: String
) 