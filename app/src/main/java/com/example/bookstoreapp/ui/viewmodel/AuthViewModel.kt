package com.example.bookstoreapp.ui.viewmodel

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
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _authState = MutableStateFlow(AuthState.UNAUTHENTICATED)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.AUTHENTICATING
            
            try {
                val user = repository.authenticateUser(username, password)
                if (user != null) {
                    if (user.isActive) {
                        _currentUser.value = user
                        _authState.value = AuthState.AUTHENTICATED
                        _authError.value = null
                    } else {
                        _authError.value = "Account is inactive. Please contact an administrator."
                        _authState.value = AuthState.UNAUTHENTICATED
                    }
                } else {
                    _authError.value = "Invalid username or password"
                    _authState.value = AuthState.UNAUTHENTICATED
                }
            } catch (e: Exception) {
                _authError.value = "Error: ${e.message}"
                _authState.value = AuthState.UNAUTHENTICATED
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.UNAUTHENTICATED
        _authError.value = null
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