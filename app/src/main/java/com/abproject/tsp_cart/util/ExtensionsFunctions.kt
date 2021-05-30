package com.abproject.tsp_cart.util

import android.net.Uri
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import com.abproject.tsp_cart.model.dataclass.Product
import com.bumptech.glide.Glide

fun ImageView.loadImage(
    uri: Uri,
) {
    Glide.with(this)
        .load(uri)
        .into(this)
}

fun TextView.loadProductPrice(
    product: Product,
) {
    if (product.discountedProductPrice == "0")
        this.text = product.productPrice
    else
        this.text = product.discountedProductPrice
}

fun CharSequence.checkEmailIsValid() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()