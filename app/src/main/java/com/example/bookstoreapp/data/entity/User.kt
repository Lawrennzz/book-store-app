package com.example.bookstoreapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    ADMIN, MANAGER, CASHIER, STAFF
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val username: String,
    val password: String, // Store hashed passwords only
    val fullName: String,
    val email: String,
    val role: UserRole,
    val isActive: Boolean = true
) 