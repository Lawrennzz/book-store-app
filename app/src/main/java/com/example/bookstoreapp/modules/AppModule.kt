package com.example.bookstoreapp.modules

import androidx.room.Room
import com.example.bookstoreapp.data.database.AppDatabase
import com.example.bookstoreapp.data.repository.ItemRepository
import com.example.bookstoreapp.ui.viewmodel.ItemViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "bookstore_db"
        ).build()
    }
    single { get<AppDatabase>().itemDao() }
    single { ItemRepository(get()) }
    viewModel { ItemViewModel(get()) }
}