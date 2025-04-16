package com.example.bookstoreapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bookstoreapp.data.dao.ItemDao
import com.example.bookstoreapp.data.entity.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}