package com.abproject.tsp_cart.model.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.util.Variables
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val sharedPreferences: SharedPreferences,
) : CartRepository {

    override suspend fun insertCart(cart: Cart): Boolean {
        val checkProductIsExist = searchInProductsByProductNameInCart(cart.productName)
        if (checkProductIsExist != null)
            return false
        cartDao.insertCart(cart)
        return true
    }

    override suspend fun updateCart(cart: Cart) {
        return cartDao.updateCart(cart)
    }

    override suspend fun deleteCart(cart: Cart) {
        return cartDao.deleteCart(cart)
    }

    override suspend fun getAllProductsFromCartByUsername(username: String): List<Cart> {
        return cartDao.getAllProductsFromCartByUsername(username)
    }

    override suspend fun searchInProductsByProductNameInCart(productName: String): Cart? {
        return cartDao.searchInProductsByProductNameInCart(productName)
    }

    override fun getUsernameFromSharedPrefs(): String {
        val username = sharedPreferences.getString(Variables.SHARED_KEY_USERNAME, null)
        return username ?: ""
    }
}