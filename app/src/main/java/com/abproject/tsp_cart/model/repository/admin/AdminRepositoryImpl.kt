package com.abproject.tsp_cart.model.repository.admin

import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.dataclass.UserData
import com.abproject.tsp_cart.model.repository.BaseRepository
import javax.inject.Inject

class AdminRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val baseRepository: BaseRepository,
) : AdminRepository {

    override suspend fun insertProduct(
        product: Product,
    ): Boolean {
        val result = searchForExistingProduct(product.productTitle)
        return if (!result) {
            productDao.insertProduct(product)
            true
        } else
            false
    }

    override suspend fun updateProduct(
        product: Product,
    ) {
        return productDao.updateProduct(product)
    }

    override suspend fun deleteProduct(
        product: Product,
    ) {
        return productDao.deleteProduct(product)
    }

    override suspend fun getAllProducts(): List<Product> {
        return productDao.getAllProductsWithUsername(UserData.username!!)
    }

    override suspend fun searchInDatabaseByProductTitle(
        query: String,
    ): List<Product> {
        return productDao.searchInDatabaseByProductTitle(query)
    }

    override suspend fun searchForExistingProduct(
        productTitle: String,
    ): Boolean {
        val response = productDao.searchForExistingProduct(productTitle)
        return response != null
    }

    override fun logout() {
        baseRepository.clearSharedPrefs()
        baseRepository.clearUserData()
    }
}