package com.example.bookstoreapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookstoreapp.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextDecoration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val authError by authViewModel.authError.collectAsState()
    val context = LocalContext.current
    var localError by remember { mutableStateOf<String?>(null) }
    var resetDialogVisible by remember { mutableStateOf(false) }
    var isResetting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(authState) {
        if (authState == AuthViewModel.AuthState.AUTHENTICATED) {
            onLoginSuccess()
        }
    }
    
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginAttempts by remember { mutableStateOf(0) }
    
    if (resetDialogVisible) {
        AlertDialog(
            onDismissRequest = { resetDialogVisible = false },
            title = { Text("Reset Admin Account") },
            text = { Text("This will reset the admin account to default credentials. Do you want to continue?") },
            confirmButton = {
                Button(
                    onClick = {
                        isResetting = true
                        coroutineScope.launch {
                            try {
                                authViewModel.resetAdminUser()
                                Toast.makeText(context, "Admin account has been reset. Use 'admin'/'admin123' to login.", Toast.LENGTH_LONG).show()
                                username = "admin"
                                password = "admin123"
                                isResetting = false
                                resetDialogVisible = false
                            } catch (e: Exception) {
                                Toast.makeText(context, "Reset failed: ${e.message}", Toast.LENGTH_LONG).show()
                                isResetting = false
                            }
                        }
                    },
                    enabled = !isResetting
                ) {
                    if (isResetting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Reset")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { resetDialogVisible = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bookstore Manager",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        if (authError != null) {
            Text(
                text = authError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        if (localError != null) {
            Text(
                text = localError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            Log.e("LoginScreen", "Error during login", exception)
            localError = "App error: ${exception.message ?: "Unknown error"}"
            Toast.makeText(context, "Login error: ${exception.message}", Toast.LENGTH_LONG).show()
            loginAttempts++
        }
        
        Button(
            onClick = { 
                localError = null
                coroutineScope.launch(exceptionHandler) {
                    try {
                        authViewModel.login(username, password)
                        if (authViewModel.authState.value != AuthViewModel.AuthState.AUTHENTICATED) {
                            loginAttempts++
                        }
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Error during login button click", e)
                        localError = "Login error: ${e.message ?: "Unknown error"}"
                        Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
                        loginAttempts++
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = username.isNotBlank() && password.isNotBlank() &&
                     authState != AuthViewModel.AuthState.AUTHENTICATING
        ) {
            if (authState == AuthViewModel.AuthState.AUTHENTICATING) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = onNavigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account? Create one")
        }
        
        // Show the reset option after 3 failed attempts
        if (loginAttempts >= 3) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Having trouble? Reset admin account",
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { resetDialogVisible = true }
            )
        }
    }
} 