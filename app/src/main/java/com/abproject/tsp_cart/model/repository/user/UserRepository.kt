package com.abproject.tsp_cart.model.repository.user

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product

interface UserRepository {

    suspend fun addProductToCart(
        cart: Cart,
    ): Boolean

    fun getAllProducts(): LiveData<List<Product>>

    suspend fun searchInProductsByProductNameInCart(
        productName: String,
    ): Cart?

    fun logout()
}