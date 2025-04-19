package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.User
import com.example.bookstoreapp.data.entity.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
    
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
    
    @Query("SELECT * FROM users WHERE role = :role")
    fun getUsersByRole(role: UserRole): Flow<List<User>>
    
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Query("UPDATE users SET isActive = :isActive WHERE username = :username")
    suspend fun updateUserActiveStatus(username: String, isActive: Boolean)
    
    @Delete
    suspend fun deleteUser(user: User)

    @Query("UPDATE users SET password = :hashedPassword WHERE username = :username")
    suspend fun updateUserPassword(username: String, hashedPassword: String)

    @Query("UPDATE users SET role = :newRole WHERE username = :username")
    suspend fun updateUserRole(username: String, newRole: UserRole)

    @Query("SELECT * FROM users WHERE isActive = 1")
    fun getActiveUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE isActive = 0")
    fun getInactiveUsers(): Flow<List<User>>

    @Query("SELECT COUNT(*) FROM users WHERE isActive = 0")
    suspend fun countInactiveUsers(): Int
} 