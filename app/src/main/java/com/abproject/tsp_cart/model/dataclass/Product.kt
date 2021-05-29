package com.abproject.tsp_cart.model.dataclass

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tbl_product")
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var productId: Int? = null,
    @ColumnInfo(name = "product_title")
    var productTitle: String,
    @ColumnInfo(name = "thumbnail_picture")
    var thumbnailPicture: String,
    @ColumnInfo(name = "product_pictures")
    var productPictures: List<String>,
    @ColumnInfo(name = "product_price")
    var productPrice: String,
    @ColumnInfo(name = "discounted_product_price")
    var discountedProductPrice: String,
    @ColumnInfo(name = "product_inventory")
    var productInventory: Long,
    @ColumnInfo(name = "product_sold")
    var productSold: Long,
) : Parcelable
