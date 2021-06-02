package com.abproject.tsp_cart.base

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.forEach
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.totalPriceGenerator


/**
 * Created by Abolfazl on 5/22/21
 */
abstract class TSPActivity : AppCompatActivity(), TSPInterface {
    override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is CoordinatorLayout) {
                viewGroup.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("rootView must be instance of CoordinatorLayout!")
            } else
                return viewGroup
        }
    override val viewContext: Context?
        get() = this

    open fun convertProductToCartForSave(
        product: Product,
        username: String,
        amount: Int,
    ): Cart {
        return Cart(
            productName = product.productTitle,
            userName = username,
            thumbnailPicture = product.thumbnailPicture,
            productPictures = product.productPictures,
            amount = amount,
            productPrice = product.productPrice,
            productDiscountedPrice = product.discountedProductPrice,
            totalPrice = totalPriceGenerator(
                amount,
                product.productPrice
            ) ?: ""
        )
    }
}