package com.abproject.tsp_cart.view.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.repository.admin.AdminRepository
import com.abproject.tsp_cart.model.repository.cart.CartRepository
import com.abproject.tsp_cart.model.repository.user.UserRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
) : TSPViewModel() {

    private val _addToCartStatus = MutableLiveData<Resource<Boolean>>()

    val getAllProducts = userRepository.getAllProducts()
    val addToCartStatus: LiveData<Resource<Boolean>> get() = _addToCartStatus

    fun addToCart(
        cart: Cart,
    ) {
        val addToCartJob = viewModelScope.launch {
            _addToCartStatus.postValue(Resource.Loading())
            val result = cartRepository.insertCart(cart)
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

    fun logout() {
        userRepository.logout()
    }
}