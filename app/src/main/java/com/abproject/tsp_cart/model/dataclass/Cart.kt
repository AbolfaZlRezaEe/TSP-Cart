package com.abproject.tsp_cart.model.dataclass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_cart")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "purchase_id")
    var purchaseId: Int? = null,
    @ColumnInfo(name = "product_name")
    var productName: String,
    @ColumnInfo(name = "user_name")
    var userName: String,
    @ColumnInfo(name = "product_owner")
    var productOwner: String,
    @ColumnInfo(name = "product_owner_email")
    var productOwnerEmail: String,
    @ColumnInfo(name = "thumbnail_picture")
    var thumbnailPicture: String,
    @ColumnInfo(name = "product_pictures")
    var productPictures: List<String>,
    var amount: Int,
    @ColumnInfo(name = "product_inventory")
    var productInventory: Long,
    @ColumnInfo(name = "product_sold")
    var productSold: Long,
    @ColumnInfo(name = "product_price")
    var productPrice: Long,
    @ColumnInfo(name = "product_discounted_price")
    var productDiscountedPrice: Long,
    @ColumnInfo(name = "progressbar_visible")
    var progressBarVisible: Boolean = false,
) {
}