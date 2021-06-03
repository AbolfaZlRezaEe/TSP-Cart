package com.abproject.tsp_cart.view.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.repository.AdminRepository
import com.abproject.tsp_cart.model.repository.UserRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository,
) : TSPViewModel() {

    val getAllProducts = adminRepository.getAllProducts()

    private val _searchInProductResult = MutableLiveData<Resource<List<Product>>>()

    val searchInProductResult: LiveData<Resource<List<Product>>> get() = _searchInProductResult

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

    fun getUserName(): String {
        return userRepository.getUsernameFromShredPrefs()
    }

    fun getEmail(): String {
        return userRepository.getEmailFromSharedPrefs()
    }

    fun clearAdminInformation() {
        adminRepository.clearAdminInformation()
    }
}