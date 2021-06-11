package com.abproject.tsp_cart.model.repository.cart

import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.UserData
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
) : CartRepository {

    override suspend fun insertCart(
        cart: Cart,
    ): Boolean {
        val checkProductIsExist = searchInProductsByProductNameInCart(cart.productName)
        if (checkProductIsExist != null)
            return false
        cartDao.insertCart(cart)
        return true
    }

    override suspend fun updateCart(
        cart: Cart,
    ) {
        return cartDao.updateCart(cart)
    }

    override suspend fun deleteCart(
        cart: Cart,
    ) {
        return cartDao.deleteCart(cart)
    }

    override suspend fun getAllProductsFromCartByUsername(): List<Cart> {
        return cartDao.getAllProductsFromCartByUsername(UserData.username!!)
    }

    override suspend fun searchInProductsByProductNameInCart(
        productName: String,
    ): Cart? {
        return cartDao.searchInProductsByProductNameInCart(productName)
    }
}