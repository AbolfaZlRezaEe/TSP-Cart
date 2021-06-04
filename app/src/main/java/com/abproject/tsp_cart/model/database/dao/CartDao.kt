package com.abproject.tsp_cart.model.database.dao

import androidx.room.*
import com.abproject.tsp_cart.model.dataclass.Cart

@Dao
interface CartDao {

    @Insert
    suspend fun insertCart(cart: Cart)

    @Update
    suspend fun updateCart(cart: Cart)

    @Delete
    suspend fun deleteCart(cart: Cart)

    /**
     * this function takes a username for searching in database.
     * if username has already exists @return List<Cart> and if this
     * username didn't exist so @return emptyList.
     */
    @Query("SELECT * FROM tbl_cart WHERE user_name == :userName")
    suspend fun getAllProductsFromCartByUsername(userName: String): List<Cart>

    @Query("SELECT * FROM tbl_cart WHERE product_name == :productName")
    suspend fun searchInProductsByProductNameInCart(productName: String): Cart?
}