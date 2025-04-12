package com.example.bookstoreapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val quantity: Int,
    val price: Double,
    val category: String
) 