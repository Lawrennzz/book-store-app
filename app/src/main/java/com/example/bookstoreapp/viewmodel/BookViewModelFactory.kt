package com.example.bookstoreapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookstoreapp.data.BookDao
import com.example.bookstoreapp.data.AppDatabase

class BookViewModelFactory(
    private val bookDao: BookDao
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddBookViewModel::class.java) -> {
                AddBookViewModel(bookDao) as T
            }
            modelClass.isAssignableFrom(EditBookViewModel::class.java) -> {
                EditBookViewModel(bookDao) as T
            }
            modelClass.isAssignableFrom(BookListViewModel::class.java) -> {
                BookListViewModel(bookDao) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
} 