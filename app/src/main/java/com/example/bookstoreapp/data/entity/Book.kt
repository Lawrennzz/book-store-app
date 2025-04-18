package com.example.bookstoreapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val isbn: String,
    val publisher: String,
    val edition: String,
    val genre: String,
    val price: Double,
    val quantity: Int,
    val threshold: Int, // For low stock alerts
    val coverImageUri: String = "",
    val dateAdded: Long = System.currentTimeMillis()
) 