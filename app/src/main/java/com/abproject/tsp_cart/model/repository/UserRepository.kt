package com.abproject.tsp_cart.model.repository

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product

interface UserRepository {

    suspend fun insertCart(cart: Cart): Boolean
    fun getAllProducts(): LiveData<List<Product>>
    fun getUsernameFromShredPrefs(): String
    fun getEmailFromSharedPrefs(): String
    suspend fun searchInProductsByProductNameInCart(productName: String): Cart?
}