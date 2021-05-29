package com.abproject.tsp_cart.model.repository

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.database.ProductDao
import com.abproject.tsp_cart.model.dataclass.Product
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
) : AdminRepository {

    override suspend fun insertProduct(product: Product): Boolean {
        val result = searchForExistingProduct(product.productTitle)
        return if (!result) {
            productDao.insertProduct(product)
            true
        } else
            false
    }

    override suspend fun updateProduct(product: Product) {
        return productDao.updateProduct(product)
    }

    override suspend fun deleteProduct(product: Product) {
        return productDao.deleteProduct(product)
    }

    override fun getAllProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    override suspend fun searchInDatabaseByProductTitle(query: String): List<Product> {
        return productDao.searchInDatabaseByProductTitle(query)
    }

    override suspend fun searchForExistingProduct(productTitle: String): Boolean {
        val response = productDao.searchForExistingProduct(productTitle)
        return response != null
    }
}