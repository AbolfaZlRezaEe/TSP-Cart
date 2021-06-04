package com.abproject.tsp_cart.view.cart

import com.abproject.tsp_cart.model.dataclass.Cart


interface CartAdapterCallBacks {

    fun onRemoveItemFromCart(cart: Cart)
    fun onIncreaseItemCart(cart: Cart)
    fun onDecreaseItemCart(cart: Cart)
    fun onProductClick(cart: Cart)

}