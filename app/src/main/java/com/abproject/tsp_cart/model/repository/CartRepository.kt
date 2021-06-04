package com.abproject.tsp_cart.model.repository

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.dataclass.Cart

interface CartRepository {

    suspend fun insertCart(cart: Cart): Boolean
    suspend fun updateCart(cart: Cart)
    suspend fun deleteCart(cart: Cart)
    suspend fun getAllProductsFromCartByUsername(username: String): List<Cart>
    suspend fun searchInProductsByProductNameInCart(productName: String): Cart?
    fun getUsernameFromSharedPrefs():String
}