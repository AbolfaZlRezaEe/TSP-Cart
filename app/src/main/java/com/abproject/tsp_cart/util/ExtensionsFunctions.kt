package com.abproject.tsp_cart.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Patterns
import android.widget.ImageView
import android.widget.TextView
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.bumptech.glide.Glide
import timber.log.Timber
import java.io.File
import java.lang.Exception

fun ImageView.loadImage(
    uri: Uri,
) {
    Glide.with(this)
        .load(uri)
        .into(this)
}

fun loadProductPrice(
    textView: TextView? = null,
    product: Product,
) {
    if (product.discountedProductPrice == "0")
        textView?.text = product.productPrice
    else
        textView?.text = product.discountedProductPrice
}


fun CharSequence.checkEmailIsValid() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun totalPriceGenerator(
    amount: Int,
    productPrice: String,
): String? {
    return try {
        val result = amount * productPrice.toLong()
        result.toString()
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun getHeightFromImageUri(
    uri: Uri,
    context: Context,
): Int {
    val option = BitmapFactory.Options()
    option.inJustDecodeBounds = true
    BitmapFactory.decodeStream(
        context.contentResolver.openInputStream(uri),
        null,
        option)
    return option.outHeight
}
