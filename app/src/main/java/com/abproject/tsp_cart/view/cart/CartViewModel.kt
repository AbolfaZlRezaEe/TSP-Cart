package com.abproject.tsp_cart.view.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.PurchaseDetail
import com.abproject.tsp_cart.model.repository.cart.CartRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
) : TSPViewModel() {

    private val _getAllProductsFromCart = MutableLiveData<Resource<List<Cart>>>()
    private val _updateCartStatus = MutableLiveData<Resource<Boolean>>()
    private val _deleteCartStatus = MutableLiveData<Resource<Boolean>>()
    private val _purchaseDetailStatus = MutableLiveData<PurchaseDetail>()

    val updateCartStatus: LiveData<Resource<Boolean>> get() = _updateCartStatus
    val deleteCartStatus: LiveData<Resource<Boolean>> get() = _deleteCartStatus
    val purchaseDetailStatus: LiveData<PurchaseDetail> get() = _purchaseDetailStatus
    val getAllProductsFromCart: LiveData<Resource<List<Cart>>> get() = _getAllProductsFromCart

    fun updateCart(
        cart: Cart,
    ) {
        val updateCartJob = viewModelScope.launch {
            _updateCartStatus.postValue(Resource.Loading())
            cartRepository.updateCart(cart)
            _updateCartStatus.postValue(Resource.Success(
                true,
                null
            ))
        }
        updateCartJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _updateCartStatus.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun increaseCartItem(
        cart: Cart,
    ) {
        _updateCartStatus.postValue(Resource.Loading())
        cart.amount++
        val updateJob = viewModelScope.launch {
            cartRepository.updateCart(cart)
            _updateCartStatus.postValue(Resource.Success(
                true,
                null))
            cart.progressBarVisible = false
            getAllProductsFromCart()
        }
        updateJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _updateCartStatus.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun decreaseCartItem(
        cart: Cart,
    ) {
        if (cart.amount > 1) {
            _updateCartStatus.postValue(Resource.Loading())
            cart.amount--
            val updateJob = viewModelScope.launch {
                cartRepository.updateCart(cart)
                _updateCartStatus.postValue(Resource.Success(
                    true,
                    null
                ))
                cart.progressBarVisible = false
                getAllProductsFromCart()
            }
            updateJob.invokeOnCompletion { throwable ->
                throwable?.message?.let {
                    _updateCartStatus.postValue(Resource.Error(null,
                        "Unexpected error occurred!"))
                }
            }
        } else {
            _updateCartStatus.postValue(Resource.Error(
                null,
                "You Can't Decrease Product Item anymore!"
            ))
        }
    }

    fun getAllProductsFromCart() {
        val getAllProductsJob = viewModelScope.launch {
            _getAllProductsFromCart.postValue(Resource.Loading())
            val response = cartRepository.getAllProductsFromCartByUsername()
            if (response.isNullOrEmpty())
                _getAllProductsFromCart.postValue(Resource.EmptyState())
            else {
                _getAllProductsFromCart.postValue(Resource.Success(
                    response,
                    null
                ))
                calculatePrices(response)
            }
        }
        getAllProductsJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _getAllProductsFromCart.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    private fun calculatePrices(
        products: List<Cart>,
    ) {
        var payablePrice = 0L
        var discountPrice = 0L
        var totalPrice = 0L
        products.map { cart ->
            payablePrice += cart.productPrice * cart.amount
            discountPrice += cart.productDiscountedPrice * cart.amount
        }
        totalPrice = discountPrice
        _purchaseDetailStatus.postValue(
            PurchaseDetail(
                totalPrice = totalPrice,
                discountPrice = discountPrice,
                payablePrice = payablePrice
            )
        )
    }

    fun deleteProductFromCart(
        cart: Cart,
    ) {
        val deleteCartJob = viewModelScope.launch {
            _deleteCartStatus.postValue(Resource.Loading())
            cartRepository.deleteCart(cart)
            _deleteCartStatus.postValue(Resource.Success(
                true,
                "product deleted from the cart successfully!"
            ))
            getAllProductsFromCart()
        }
        deleteCartJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _deleteCartStatus.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

}