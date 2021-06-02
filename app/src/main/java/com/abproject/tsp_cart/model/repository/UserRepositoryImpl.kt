package com.abproject.tsp_cart.model.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_USERNAME
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val productDao: ProductDao,
    private val sharedPreferences: SharedPreferences,
) : UserRepository {

    override suspend fun insertProductToCart(cart: Cart): Boolean {
        val checkProductIsExist = searchInProductsByProductName(cart.productName)
        if (checkProductIsExist != null)
            return false
        cartDao.insertCart(cart)
        return true
    }

    override suspend fun updateProductToCart(cart: Cart) {
        return cartDao.updateCart(cart)
    }

    override suspend fun deleteProductToCart(cart: Cart) {
        return cartDao.deleteCart(cart)
    }

    override fun getAllProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    override suspend fun getAllProductsByUsername(username: String): List<Cart> {
        return cartDao.getAllProductsByUsername(username)
    }

    override suspend fun searchInProductsByProductName(productName: String): Cart? {
        return cartDao.searchInProductsByProductName(productName)
    }

    override fun getUsernameFromShredPrefs(): String {
        val username = sharedPreferences.getString(SHARED_KEY_USERNAME, null)
        return username ?: ""
    }
}