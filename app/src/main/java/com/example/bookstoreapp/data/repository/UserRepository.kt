package com.example.bookstoreapp.data.repository

import android.util.Log
import com.example.bookstoreapp.data.dao.UserDao
import com.example.bookstoreapp.data.entity.User
import com.example.bookstoreapp.data.entity.UserRole
import com.example.bookstoreapp.util.PasswordHasher
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    private val TAG = "UserRepository"
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    
    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
    
    fun getUsersByRole(role: UserRole): Flow<List<User>> {
        return userDao.getUsersByRole(role)
    }
    
    suspend fun insertUser(user: User) {
        Log.d(TAG, "Inserting new user: ${user.username}")
        val hashedPassword = PasswordHasher.hashPassword(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)
        userDao.insertUser(userWithHashedPassword)
    }
    
    suspend fun updateUser(user: User) {
        Log.d(TAG, "Updating user: ${user.username}")
        // Only hash the password if it's been changed (doesn't contain a salt)
        val finalUser = if (!user.password.contains(":")) {
            Log.d(TAG, "Hashing new password for user: ${user.username}")
            user.copy(password = PasswordHasher.hashPassword(user.password))
        } else {
            user
        }
        userDao.updateUser(finalUser)
    }
    
    suspend fun updateUserActiveStatus(username: String, isActive: Boolean) {
        Log.d(TAG, "Updating active status for user: $username to: $isActive")
        userDao.updateUserActiveStatus(username, isActive)
    }
    
    suspend fun deleteUser(user: User) {
        Log.d(TAG, "Deleting user: ${user.username}")
        userDao.deleteUser(user)
    }
    
    suspend fun authenticateUser(username: String, password: String): User? {
        Log.d(TAG, "Starting authentication for username: $username")
        val user = userDao.getUserByUsername(username)
        
        if (user == null) {
            Log.d(TAG, "User not found: $username")
            return null
        }
        
        Log.d(TAG, "Found user: $username, isActive: ${user.isActive}, stored password format: ${if (user.password.contains(":")) "hashed" else "plain"}")
        
        if (!user.isActive) {
            Log.d(TAG, "User is inactive: $username")
            return null
        }
        
        // Handle the case where the password hasn't been hashed yet (legacy data)
        val isAuthenticated = if (user.password.contains(":")) {
            // Normal case - verify hashed password
            val verified = PasswordHasher.verifyPassword(password, user.password)
            Log.d(TAG, "Verifying hashed password for $username: ${if (verified) "success" else "failed"}")
            verified
        } else {
            // Legacy case - direct comparison and update to hashed
            Log.d(TAG, "Found plain password for $username, attempting direct comparison")
            if (user.password == password) {
                Log.d(TAG, "Plain password matched, upgrading to hashed for $username")
                // Update to hashed password
                val hashedPassword = PasswordHasher.hashPassword(password)
                userDao.updateUserPassword(username, hashedPassword)
                true
            } else {
                Log.d(TAG, "Plain password comparison failed for $username")
                false
            }
        }
        
        return if (isAuthenticated) {
            Log.d(TAG, "Authentication successful for user: $username")
            user
        } else {
            Log.d(TAG, "Authentication failed for user: $username")
            null
        }
    }

    suspend fun updateUserPassword(username: String, newPassword: String) {
        Log.d(TAG, "Updating password for user: $username")
        val hashedPassword = PasswordHasher.hashPassword(newPassword)
        userDao.updateUserPassword(username, hashedPassword)
    }

    suspend fun updateUserRole(username: String, newRole: UserRole) {
        Log.d(TAG, "Updating role for user: $username to: $newRole")
        userDao.updateUserRole(username, newRole)
    }

    fun getActiveUsers(): Flow<List<User>> = userDao.getActiveUsers()
    
    fun getInactiveUsers(): Flow<List<User>> = userDao.getInactiveUsers()
    
    suspend fun countPendingApprovals(): Int = userDao.countInactiveUsers()
    
    /**
     * Reset admin user in case of corruption or authentication issues
     */
    suspend fun resetAdminUser() {
        Log.d(TAG, "Attempting to reset admin user")
        try {
            // Check if admin exists
            val adminUser = userDao.getUserByUsername("admin")
            
            // Delete if exists
            if (adminUser != null) {
                Log.d(TAG, "Found existing admin user, deleting it")
                userDao.deleteUser(adminUser)
            }
            
            // Create new admin
            Log.d(TAG, "Creating fresh admin user")
            val hashedPassword = PasswordHasher.hashPassword("admin123")
            val admin = User(
                username = "admin",
                password = hashedPassword,
                fullName = "Administrator",
                email = "admin@bookstore.com",
                role = UserRole.ADMIN,
                isActive = true
            )
            userDao.insertUser(admin)
            Log.d(TAG, "Admin user reset successful")
        } catch (e: Exception) {
            Log.e(TAG, "Error resetting admin user", e)
            throw e
        }
    }
} 