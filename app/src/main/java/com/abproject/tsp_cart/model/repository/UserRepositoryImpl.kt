package com.abproject.tsp_cart.model.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_EMAIL
import com.abproject.tsp_cart.util.Variables.SHARED_KEY_USERNAME
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val sharedPreferences: SharedPreferences,
) : UserRepository {

    override suspend fun insertCart(cart: Cart): Boolean {
        val checkProductIsExist = searchInProductsByProductNameInCart(cart.productName)
        if (checkProductIsExist != null)
            return false
        cartDao.insertCart(cart)
        return true
    }

    override fun getAllProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    override fun getUsernameFromShredPrefs(): String {
        val username = sharedPreferences.getString(SHARED_KEY_USERNAME, null)
        return username ?: ""
    }

    override fun getEmailFromSharedPrefs(): String {
        val email = sharedPreferences.getString(SHARED_KEY_EMAIL, null)
        return email ?: ""
    }

    override suspend fun searchInProductsByProductNameInCart(productName: String): Cart? {
        return cartDao.searchInProductsByProductNameInCart(productName)
    }
}