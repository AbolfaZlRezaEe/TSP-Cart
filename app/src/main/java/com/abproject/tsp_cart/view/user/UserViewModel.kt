package com.abproject.tsp_cart.view.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.repository.UserRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : TSPViewModel() {

    private val _getAllProductsFromCart = MutableLiveData<Resource<List<Cart>>>()
    private val _addToCartStatus = MutableLiveData<Resource<Boolean>>()
    private val _updateCartStatus = MutableLiveData<Resource<Boolean>>()
    private val _deleteCartStatus = MutableLiveData<Resource<Boolean>>()

    val getAllProducts = userRepository.getAllProducts()
    val getAllProductsFromCart: LiveData<Resource<List<Cart>>> get() = _getAllProductsFromCart
    val addToCartStatus: LiveData<Resource<Boolean>> get() = _addToCartStatus
    val updateCartStatus: LiveData<Resource<Boolean>> get() = _updateCartStatus
    val deleteCartStatus: LiveData<Resource<Boolean>> get() = _deleteCartStatus

    fun getAllProductsInCart(
        username: String,
    ) {
        val getAllProductsJob = viewModelScope.launch {
            _getAllProductsFromCart.postValue(Resource.Loading())
            val response = userRepository.getAllProductsByUsername(username)
            if (response.isNullOrEmpty())
                _getAllProductsFromCart.postValue(Resource.EmptyState())
            else
                _getAllProductsFromCart.postValue(Resource.Success(
                    response,
                    null
                ))
        }
        getAllProductsJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _getAllProductsFromCart.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun addToCart(
        cart: Cart,
    ) {
        val addToCartJob = viewModelScope.launch {
            _addToCartStatus.postValue(Resource.Loading())
            val result = userRepository.insertProductToCart(cart)
            if (result)
                _addToCartStatus.postValue(Resource.Success(
                    true,
                    "product added to the cart successfully!"
                ))
            else
                _addToCartStatus.postValue(Resource.Error(
                    null,
                    "This product has been added before!"
                ))

        }
        addToCartJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _addToCartStatus.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun deleteCart(
        cart: Cart,
    ) {
        val deleteCartJob = viewModelScope.launch {
            _deleteCartStatus.postValue(Resource.Loading())
            userRepository.deleteProductToCart(cart)
            _deleteCartStatus.postValue(Resource.Success(
                true,
                "product deleted from the cart successfully!"
            ))
        }
        deleteCartJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _deleteCartStatus.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun updateCart(
        cart: Cart,
    ) {
        val updateCartJob = viewModelScope.launch {
            _updateCartStatus.postValue(Resource.Loading())
            userRepository.updateProductToCart(cart)
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

    fun getUserName(): String {
        return userRepository.getUsernameFromShredPrefs()
    }

}