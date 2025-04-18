package com.example.bookstoreapp.data.repository

import com.example.bookstoreapp.data.dao.SupplierDao
import com.example.bookstoreapp.data.entity.Supplier
import kotlinx.coroutines.flow.Flow

class SupplierRepository(private val supplierDao: SupplierDao) {
    val allSuppliers: Flow<List<Supplier>> = supplierDao.getAllSuppliers()
    
    suspend fun getSupplierById(id: Int): Supplier? {
        return supplierDao.getSupplierById(id)
    }
    
    suspend fun insertSupplier(supplier: Supplier): Long {
        return supplierDao.insertSupplier(supplier)
    }
    
    suspend fun updateSupplier(supplier: Supplier) {
        supplierDao.updateSupplier(supplier)
    }
    
    suspend fun deleteSupplier(supplier: Supplier) {
        supplierDao.deleteSupplier(supplier)
    }
    
    fun searchSuppliers(query: String): Flow<List<Supplier>> {
        return supplierDao.searchSuppliers("%$query%")
    }
} 