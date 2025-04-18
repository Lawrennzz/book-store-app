package com.example.bookstoreapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val email: String,
    val address: String,
    val membershipLevel: String = "STANDARD", // STANDARD, SILVER, GOLD, etc.
    val loyaltyPoints: Int = 0,
    val dateJoined: Long = System.currentTimeMillis()
) 