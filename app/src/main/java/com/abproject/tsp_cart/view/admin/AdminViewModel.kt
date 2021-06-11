package com.abproject.tsp_cart.view.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.dataclass.UserData
import com.abproject.tsp_cart.model.repository.admin.AdminRepository
import com.abproject.tsp_cart.model.repository.user.UserRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
) : TSPViewModel() {


    private val _searchInProductResult = MutableLiveData<Resource<List<Product>>>()
    private val _getAllProducts = MutableLiveData<Resource<List<Product>>>()

    val searchInProductResult: LiveData<Resource<List<Product>>> get() = _searchInProductResult
    val getAllProducts: LiveData<Resource<List<Product>>> get() = _getAllProducts

    fun getAllProduct() {
        val getAllJob = viewModelScope.launch {
            _getAllProducts.postValue(Resource.Loading())
            val result = adminRepository.getAllProducts()
            if (result.isNullOrEmpty())
                _getAllProducts.postValue(Resource.EmptyState())
            else
                _getAllProducts.postValue(Resource.Success(
                    result,
                    null
                ))
        }
        getAllJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _getAllProducts.postValue(Resource.Error(null, "Unexpected error occurred!"))
            }
        }

    }

    fun searchInProducts(productTitle: String) {
        val searchJob = viewModelScope.launch {
            _searchInProductResult.postValue(Resource.Loading())
            val result = adminRepository.searchInDatabaseByProductTitle(productTitle)
            if (result.isNullOrEmpty())
                _searchInProductResult.postValue(Resource.EmptyState())
            else
                _searchInProductResult.postValue(Resource.Success(result, null))
        }
        searchJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _searchInProductResult.postValue(Resource.Error(null, "Unexpected error occurred!"))
            }
        }
    }

    fun getUsernameAndEmail(): Pair<String, String> {
        val username = UserData.username!!
        val email = UserData.email!!
        return Pair(username, email)
    }

    fun logout() {
        adminRepository.logout()
    }
}