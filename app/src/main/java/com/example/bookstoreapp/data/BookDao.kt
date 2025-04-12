package com.example.bookstoreapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBook(id: String)

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<Book>>

    @Query("SELECT COUNT(*) FROM books")
    fun getTotalBooks(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE quantity <= 5")
    fun getLowStockBooksCount(): Flow<Int>
} 