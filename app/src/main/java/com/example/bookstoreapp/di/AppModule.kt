package com.example.bookstoreapp.di

import android.content.Context
import androidx.room.Room
import com.example.bookstoreapp.data.AppDatabase
import com.example.bookstoreapp.data.InventoryItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideInventoryItemDao(
        database: AppDatabase
    ): InventoryItemDao {
        return database.inventoryItemDao()
    }
} 