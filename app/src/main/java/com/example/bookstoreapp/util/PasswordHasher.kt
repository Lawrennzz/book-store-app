package com.example.bookstoreapp.util

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PasswordHasher {
    private const val SALT_LENGTH = 16
    
    fun hashPassword(password: String): String {
        // In a real app, you should use a proper password hashing algorithm
        // such as bcrypt, Argon2, or PBKDF2. This is a simple SHA-256 example.
        val salt = generateSalt()
        val saltedPassword = password + salt
        
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(saltedPassword.toByteArray())
        
        val saltEncoded = Base64.getEncoder().encodeToString(salt)
        val hashEncoded = Base64.getEncoder().encodeToString(hash)
        
        return "$saltEncoded:$hashEncoded"
    }
    
    private fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(SALT_LENGTH)
        random.nextBytes(salt)
        return salt
    }
    
    fun verifyPassword(storedHash: String, password: String): Boolean {
        // This is a simplified example
        // In a real app, you would extract the salt from the stored hash
        // and hash the provided password with that salt
        return storedHash == hashPassword(password)
    }
} 