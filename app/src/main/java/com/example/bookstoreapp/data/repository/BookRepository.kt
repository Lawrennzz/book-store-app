package com.example.bookstoreapp.data.repository

import com.example.bookstoreapp.data.dao.BookDao
import com.example.bookstoreapp.data.entity.Book
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()
    val lowStockBooks: Flow<List<Book>> = bookDao.getLowStockBooks()
    
    suspend fun getBookById(id: Int): Book? {
        return bookDao.getBookById(id)
    }
    
    suspend fun insertBook(book: Book): Long {
        return bookDao.insertBook(book)
    }
    
    suspend fun insertBooks(books: List<Book>): List<Long> {
        return bookDao.insertBooks(books)
    }
    
    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }
    
    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
    }
    
    fun searchBooks(query: String): Flow<List<Book>> {
        return bookDao.searchBooks("%$query%")
    }
    
    fun getBooksByGenre(genre: String): Flow<List<Book>> {
        return bookDao.getBooksByGenre(genre)
    }
    
    suspend fun decreaseStock(bookId: Int, quantitySold: Int) {
        bookDao.decreaseStock(bookId, quantitySold)
    }
    
    suspend fun increaseStock(bookId: Int, quantityAdded: Int) {
        bookDao.increaseStock(bookId, quantityAdded)
    }
} 