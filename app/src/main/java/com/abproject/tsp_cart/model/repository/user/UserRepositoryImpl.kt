package com.abproject.tsp_cart.model.repository.user

import androidx.lifecycle.LiveData
import com.abproject.tsp_cart.model.database.dao.CartDao
import com.abproject.tsp_cart.model.database.dao.ProductDao
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.repository.BaseRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val baseRepository: BaseRepository,
) : UserRepository {

    override suspend fun addProductToCart(
        cart: Cart,
    ): Boolean {
        val checkProductIsExist = searchInProductsByProductNameInCart(cart.productName)
        if (checkProductIsExist != null)
            return false
        cartDao.insertCart(cart)
        return true
    }

    override fun getAllProducts(): LiveData<List<Product>> {
        return productDao.getAllProducts()
    }

    override suspend fun searchInProductsByProductNameInCart(productName: String): Cart? {
        return cartDao.searchInProductsByProductNameInCart(productName)
    }

    override fun logout() {
        baseRepository.clearUserData()
        baseRepository.clearSharedPrefs()
    }
}