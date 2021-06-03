package com.abproject.tsp_cart.model.repository

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product

interface UserRepository {

    suspend fun insertProductToCart(cart: Cart): Boolean
    suspend fun updateProductToCart(cart: Cart)
    suspend fun deleteProductToCart(cart: Cart)
    fun getAllProducts(): LiveData<List<Product>>
    suspend fun getAllProductsByUsername(username: String): List<Cart>
    suspend fun searchInProductsByProductName(productName: String): Cart?
    fun getUsernameFromShredPrefs(): String
    fun getEmailFromSharedPrefs(): String
}