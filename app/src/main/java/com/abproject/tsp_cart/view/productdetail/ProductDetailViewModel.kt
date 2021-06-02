package com.abproject.tsp_cart.view.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.repository.AdminRepository
import com.abproject.tsp_cart.model.repository.UserRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val adminRepository: AdminRepository,
) : TSPViewModel() {

    private val _addToCartStatus = MutableLiveData<Resource<Boolean>>()
    private val _deleteCartStatus = MutableLiveData<Resource<Boolean>>()
    private val _deleteProductStatus = MutableLiveData<Resource<Boolean>>()

    val addToCartStatus: LiveData<Resource<Boolean>> get() = _addToCartStatus
    val deleteCartStatus: LiveData<Resource<Boolean>> get() = _deleteCartStatus
    val deleteProductStatus: LiveData<Resource<Boolean>> get() = _deleteProductStatus

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

    fun deleteProduct(
        product: Product,
    ) {
        val deleteProduct = viewModelScope.launch {
            _deleteProductStatus.postValue(Resource.Loading())
            adminRepository.deleteProduct(product)
            _deleteProductStatus.postValue(Resource.Success(
                true,
                "product deleted successfully!"
            ))
        }
        deleteProduct.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _deleteProductStatus.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun getUserName(): String {
        return userRepository.getUsernameFromShredPrefs()
    }
}