package com.example.bookstoreapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sales",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["username"],
            childColumns = ["cashierUsername"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("cashierUsername"),
        Index("customerId")
    ]
)
data class Sale(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val totalAmount: Double,
    val discount: Double = 0.0,
    val finalAmount: Double,
    val paymentMethod: String,
    val cashierUsername: String,
    val customerId: Int? = null
) 