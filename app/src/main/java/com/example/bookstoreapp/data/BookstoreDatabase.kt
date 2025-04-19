package com.example.bookstoreapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookstoreapp.data.dao.*
import com.example.bookstoreapp.data.entity.*
import com.example.bookstoreapp.util.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    version = 3,
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
                Log.d("BookstoreDatabase", "Creating or getting database instance")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookstoreDatabase::class.java,
                    "bookstore_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d("BookstoreDatabase", "Database created, initializing with admin user")
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                seedDatabaseWithAdminUser(database.userDao())
                            }
                        }
                    }
                })
                .build()
                
                INSTANCE = instance
                
                // Ensure admin user exists immediately after building
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("BookstoreDatabase", "Checking/creating admin user after database initialization")
                    seedDatabaseWithAdminUser(instance.userDao())
                }
                
                instance
            }
        }
        
        private suspend fun seedDatabaseWithAdminUser(userDao: UserDao) {
            try {
                // Check if admin user exists
                val adminUser = userDao.getUserByUsername("admin")
                Log.d("BookstoreDatabase", "Checking admin user: ${adminUser?.username}, exists: ${adminUser != null}")
                
                if (adminUser == null) {
                    // Create admin user if it doesn't exist
                    val hashedPassword = PasswordHasher.hashPassword("admin123")
                    Log.d("BookstoreDatabase", "Creating new admin user with hashed password format: ${hashedPassword.contains(":")}")
                    
                    val admin = User(
                        username = "admin",
                        password = hashedPassword,
                        fullName = "Administrator",
                        email = "admin@bookstore.com",
                        role = UserRole.ADMIN,
                        isActive = true
                    )
                    userDao.insertUser(admin)
                    Log.d("BookstoreDatabase", "Created new admin user successfully")
                } else {
                    // Update existing admin user if needed
                    Log.d("BookstoreDatabase", "Existing admin user found - Active: ${adminUser.isActive}, Password format: ${if (adminUser.password.contains(":")) "hashed" else "plain"}")
                    
                    if (!adminUser.isActive) {
                        userDao.updateUserActiveStatus("admin", true)
                        Log.d("BookstoreDatabase", "Activated existing admin user")
                    }
                    
                    // Check if password needs updating
                    if (!adminUser.password.contains(":")) {
                        val hashedPassword = PasswordHasher.hashPassword("admin123")
                        Log.d("BookstoreDatabase", "Updating admin password to hashed format")
                        userDao.updateUserPassword("admin", hashedPassword)
                        Log.d("BookstoreDatabase", "Updated admin password to hashed version")
                    }
                }
            } catch (e: Exception) {
                Log.e("BookstoreDatabase", "Error in seedDatabaseWithAdminUser", e)
                e.printStackTrace()
            }
        }
    }
} 