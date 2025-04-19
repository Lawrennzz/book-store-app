package com.example.bookstoreapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.bookstoreapp.ui.navigation.AppNavHost
import com.example.bookstoreapp.ui.theme.BookStoreAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.bookstoreapp.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Log start of application
        Log.d("MainActivity", "Starting app setup")
        
        setContent {
            BookStoreAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}