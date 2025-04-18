package com.example.bookstoreapp.data.dao

import androidx.room.*
import com.example.bookstoreapp.data.entity.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY title ASC")
    fun getAllBooks(): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: Int): Book?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>): List<Long>
    
    @Update
    suspend fun updateBook(book: Book)
    
    @Delete
    suspend fun deleteBook(book: Book)
    
    @Query("SELECT * FROM books WHERE quantity <= threshold")
    fun getLowStockBooks(): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE title LIKE :searchQuery OR author LIKE :searchQuery OR isbn LIKE :searchQuery")
    fun searchBooks(searchQuery: String): Flow<List<Book>>
    
    @Query("SELECT * FROM books WHERE genre = :genre")
    fun getBooksByGenre(genre: String): Flow<List<Book>>

    @Query("UPDATE books SET quantity = quantity - :quantitySold WHERE id = :bookId")
    suspend fun decreaseStock(bookId: Int, quantitySold: Int)
    
    @Query("UPDATE books SET quantity = quantity + :quantityAdded WHERE id = :bookId")
    suspend fun increaseStock(bookId: Int, quantityAdded: Int)
} 