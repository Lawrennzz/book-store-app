package com.example.bookstoreapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [InventoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inventoryItemDao(): InventoryItemDao

    companion object {
        const val DATABASE_NAME = "inventory_db"
    }
} 