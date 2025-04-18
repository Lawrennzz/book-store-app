package com.example.bookstoreapp.data.repository

import com.example.bookstoreapp.data.dao.UserDao
import com.example.bookstoreapp.data.entity.User
import com.example.bookstoreapp.data.entity.UserRole
import com.example.bookstoreapp.util.PasswordHasher
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    
    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
    
    fun getUsersByRole(role: UserRole): Flow<List<User>> {
        return userDao.getUsersByRole(role)
    }
    
    suspend fun insertUser(user: User) {
        val hashedPassword = PasswordHasher.hashPassword(user.password)
        userDao.insertUser(user.copy(password = hashedPassword))
    }
    
    suspend fun updateUser(user: User) {
        // Only hash the password if it's a new one (not already hashed)
        val currentUser = userDao.getUserByUsername(user.username)
        if (currentUser != null && currentUser.password != user.password) {
            val hashedPassword = PasswordHasher.hashPassword(user.password)
            userDao.updateUser(user.copy(password = hashedPassword))
        } else {
            userDao.updateUser(user)
        }
    }
    
    suspend fun updateUserActiveStatus(username: String, isActive: Boolean) {
        userDao.updateUserActiveStatus(username, isActive)
    }
    
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
    
    suspend fun authenticateUser(username: String, password: String): User? {
        val hashedPassword = PasswordHasher.hashPassword(password)
        return userDao.authenticateUser(username, hashedPassword)
    }
} 