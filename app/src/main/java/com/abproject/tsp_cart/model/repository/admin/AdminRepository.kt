package com.abproject.tsp_cart.model.repository.admin

import com.abproject.tsp_cart.model.dataclass.Product

interface AdminRepository {

    suspend fun insertProduct(
        product: Product,
    ): Boolean

    suspend fun updateProduct(
        product: Product,
    )

    suspend fun deleteProduct(
        product: Product,
    )

    suspend fun getAllProducts(): List<Product>

    suspend fun searchInDatabaseByProductTitle(
        query: String,
    ): List<Product>

    suspend fun searchForExistingProduct(
        productTitle: String,
    ): Boolean

    fun logout()

}