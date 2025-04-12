package com.example.bookstoreapp.di

import android.content.Context
import androidx.room.Room
import com.example.bookstoreapp.data.AppDatabase
import com.example.bookstoreapp.data.BookDao
import com.example.bookstoreapp.data.OrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bookstore.db"
        ).build()
    }

    @Provides
    fun provideBookDao(database: AppDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun provideOrderDao(database: AppDatabase): OrderDao {
        return database.orderDao()
    }
} 