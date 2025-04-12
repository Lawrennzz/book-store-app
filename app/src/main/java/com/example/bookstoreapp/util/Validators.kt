package com.example.bookstoreapp.util

object Validators {
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Name cannot be empty")
            name.length < 2 -> ValidationResult.Error("Name must be at least 2 characters")
            else -> ValidationResult.Success
        }
    }

    fun validateQuantity(quantity: String): ValidationResult {
        return try {
            val value = quantity.toInt()
            when {
                value < 0 -> ValidationResult.Error("Quantity cannot be negative")
                else -> ValidationResult.Success
            }
        } catch (e: NumberFormatException) {
            ValidationResult.Error("Invalid quantity format")
        }
    }

    fun validatePrice(price: String): ValidationResult {
        return try {
            val value = price.toDouble()
            when {
                value < 0 -> ValidationResult.Error("Price cannot be negative")
                else -> ValidationResult.Success
            }
        } catch (e: NumberFormatException) {
            ValidationResult.Error("Invalid price format")
        }
    }

    fun validateCategory(category: String): ValidationResult {
        return when {
            category.isBlank() -> ValidationResult.Error("Category cannot be empty")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
} 