package com.example.bookstoreapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [InventoryItem::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inventoryItemDao(): InventoryItemDao

    companion object {
        const val DATABASE_NAME = "inventory_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Add sample data when database is created
                        scope.launch(Dispatchers.IO) {
                            INSTANCE?.let { database ->
                                val dao = database.inventoryItemDao()
                                // Add sample items
                                dao.insertItem(
                                    InventoryItem(
                                        name = "Sample Book 1",
                                        category = "Fiction",
                                        quantity = 10,
                                        price = 19.99
                                    )
                                )
                                dao.insertItem(
                                    InventoryItem(
                                        name = "Sample Book 2",
                                        category = "Non-Fiction",
                                        quantity = 5,
                                        price = 29.99
                                    )
                                )
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 