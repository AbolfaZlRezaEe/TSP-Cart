package com.abproject.tsp_cart.model.repository

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.dataclass.Product

interface AdminRepository {

    suspend fun insertProduct(product: Product): Boolean
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
    fun getAllProducts(): LiveData<List<Product>>
    suspend fun searchInDatabaseByProductTitle(query: String): List<Product>
    suspend fun searchForExistingProduct(productTitle: String): Boolean
}