package com.example.bookstoreapp.util

object ValidationUtil {
    fun validateItem(name: String, category: String, quantity: String, price: String): Boolean {
        return name.isNotBlank() &&
                category.isNotBlank() &&
                quantity.toIntOrNull()?.let { it >= 0 } == true &&
                price.toDoubleOrNull()?.let { it >= 0.0 } == true
    }
}