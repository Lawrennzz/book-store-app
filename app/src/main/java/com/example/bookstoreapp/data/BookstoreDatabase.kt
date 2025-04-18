package com.example.bookstoreapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookstoreapp.data.dao.*
import com.example.bookstoreapp.data.entity.*

@Database(
    entities = [
        Book::class,
        User::class,
        Supplier::class,
        Customer::class,
        Sale::class,
        SaleItem::class,
        PurchaseOrder::class,
        PurchaseOrderItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BookstoreDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
    abstract fun supplierDao(): SupplierDao
    abstract fun customerDao(): CustomerDao
    abstract fun saleDao(): SaleDao
    abstract fun saleItemDao(): SaleItemDao
    abstract fun purchaseOrderDao(): PurchaseOrderDao
    abstract fun purchaseOrderItemDao(): PurchaseOrderItemDao

    companion object {
        @Volatile
        private var INSTANCE: BookstoreDatabase? = null

        fun getDatabase(context: Context): BookstoreDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookstoreDatabase::class.java,
                    "bookstore_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 