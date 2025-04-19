package com.example.bookstoreapp.util

import android.util.Log
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHasher {
    private const val ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val ITERATIONS = 65536
    private const val KEY_LENGTH = 128
    private const val SALT_LENGTH = 16
    private const val TAG = "PasswordHasher"

    fun hashPassword(password: String): String {
        try {
            if (password.isBlank()) {
                Log.e(TAG, "Cannot hash empty password")
                throw IllegalArgumentException("Password cannot be empty")
            }
            
            val random = SecureRandom()
            val salt = ByteArray(SALT_LENGTH)
            random.nextBytes(salt)

            val spec: KeySpec = PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
            )
            
            // Ensure SecretKeyFactory is available
            val factory = try {
                SecretKeyFactory.getInstance(ALGORITHM)
            } catch (e: Exception) {
                Log.e(TAG, "Algorithm $ALGORITHM not available", e)
                throw RuntimeException("Password hashing algorithm not available", e)
            }
            
            val hash = factory.generateSecret(spec).encoded

            // Combine salt and hash with a delimiter
            val saltString = Base64.getEncoder().encodeToString(salt)
            val hashString = Base64.getEncoder().encodeToString(hash)
            
            // Verify the hash format
            val finalHash = "$saltString:$hashString"
            if (!finalHash.contains(":")) {
                Log.e(TAG, "Generated hash does not contain delimiter")
                throw RuntimeException("Generated invalid hash format")
            }
            
            Log.d(TAG, "Successfully hashed password")
            return finalHash
        } catch (e: Exception) {
            Log.e(TAG, "Error hashing password", e)
            throw RuntimeException("Error hashing password", e)
        }
    }

    fun verifyPassword(password: String, storedHash: String): Boolean {
        try {
            // Handle edge cases
            if (password.isBlank()) {
                Log.e(TAG, "Attempted to verify empty password")
                return false
            }
            
            if (storedHash.isBlank()) {
                Log.e(TAG, "Stored hash is empty")
                return false
            }
            
            // Check hash format
            val parts = storedHash.split(":")
            if (parts.size != 2) {
                Log.e(TAG, "Invalid hash format: $storedHash")
                // For fallback compatibility, check if unhashed password matches
                return password == storedHash
            }

            try {
                val salt = Base64.getDecoder().decode(parts[0])
                val hash = Base64.getDecoder().decode(parts[1])

                val spec: KeySpec = PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
                )
                val factory = SecretKeyFactory.getInstance(ALGORITHM)
                val testHash = factory.generateSecret(spec).encoded

                val result = hash.contentEquals(testHash)
                Log.d(TAG, "Password verification result: $result")
                return result
            } catch (e: IllegalArgumentException) {
                // Handle Base64 decoding errors
                Log.e(TAG, "Invalid Base64 in stored hash", e)
                return false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying password", e)
            return false
        }
    }
} 