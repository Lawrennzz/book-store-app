package com.example.bookstoreapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookstoreapp.data.entity.User
import com.example.bookstoreapp.data.entity.UserRole
import com.example.bookstoreapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    private val TAG = "AuthViewModel"
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _authState = MutableStateFlow(AuthState.UNAUTHENTICATED)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    // Admin user creation has been moved to BookstoreDatabase
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.AUTHENTICATING
            _authError.value = null
            
            try {
                Log.d(TAG, "Attempting login for username: $username with password length: ${password.length}")
                val user = repository.getUserByUsername(username)
                Log.d(TAG, "Found user: ${user?.username}, role: ${user?.role}, active: ${user?.isActive}, password format: ${if (user?.password?.contains(":") == true) "hashed" else "plain"}")
                
                if (user != null) {
                    if (user.isActive) {
                        val authenticated = repository.authenticateUser(username, password)
                        Log.d(TAG, "Authentication result: ${authenticated != null}")
                        
                        if (authenticated != null) {
                            _currentUser.value = authenticated
                            _authState.value = AuthState.AUTHENTICATED
                            _authError.value = null
                            Log.d(TAG, "Login successful, user role: ${authenticated.role}")
                        } else {
                            _authError.value = "Invalid username or password"
                            _authState.value = AuthState.UNAUTHENTICATED
                            Log.d(TAG, "Authentication failed")
                        }
                    } else {
                        _authError.value = "Account is inactive. Please contact an administrator."
                        _authState.value = AuthState.UNAUTHENTICATED
                        Log.d(TAG, "Account is inactive")
                    }
                } else {
                    _authError.value = "Invalid username or password"
                    _authState.value = AuthState.UNAUTHENTICATED
                    Log.d(TAG, "User not found")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during login", e)
                _authError.value = "Error: ${e.message}"
                _authState.value = AuthState.UNAUTHENTICATED
            }
        }
    }
    
    suspend fun resetAdminUser() {
        try {
            Log.d(TAG, "Starting admin user reset")
            repository.resetAdminUser()
            Log.d(TAG, "Admin user reset completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error during admin reset", e)
            throw e
        }
    }
    
    fun registerUser(user: User, callback: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                // Check if username already exists
                val existingUser = repository.getUserByUsername(user.username)
                if (existingUser != null) {
                    callback(false, "Username already exists. Please choose another one.")
                    return@launch
                }
                
                // Insert the new user
                repository.insertUser(user)
                callback(true, null)
            } catch (e: Exception) {
                callback(false, "Registration failed: ${e.message}")
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.UNAUTHENTICATED
        _authError.value = null
        Log.d(TAG, "User logged out")
    }
    
    fun hasPermission(requiredRole: UserRole): Boolean {
        val user = _currentUser.value ?: return false
        
        // Simple role hierarchy: ADMIN > MANAGER > CASHIER > STAFF
        return when (requiredRole) {
            UserRole.ADMIN -> user.role == UserRole.ADMIN
            UserRole.MANAGER -> user.role == UserRole.ADMIN || user.role == UserRole.MANAGER
            UserRole.CASHIER -> user.role == UserRole.ADMIN || user.role == UserRole.MANAGER || user.role == UserRole.CASHIER
            UserRole.STAFF -> true // All users have at least STAFF privilege
        }
    }
    
    enum class AuthState {
        UNAUTHENTICATED,
        AUTHENTICATING,
        AUTHENTICATED
    }
    
    class AuthViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 