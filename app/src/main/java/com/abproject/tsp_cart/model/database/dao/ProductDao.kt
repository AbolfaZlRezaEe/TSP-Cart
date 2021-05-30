package com.abproject.tsp_cart.model.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.abproject.tsp_cart.model.dataclass.Product

@Dao
interface ProductDao {

    @Insert
    suspend fun insertProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM tbl_product")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM tbl_product WHERE product_title LIKE :query")
    suspend fun searchInDatabaseByProductTitle(query: String): List<Product>

    @Query("SELECT * FROM tbl_product WHERE product_title == :productTitle")
    suspend fun searchForExistingProduct(productTitle: String): Product?
}