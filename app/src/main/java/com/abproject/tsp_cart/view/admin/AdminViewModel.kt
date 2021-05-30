package com.abproject.tsp_cart.view.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.repository.AdminRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
) : TSPViewModel() {

    val getAllProducts = adminRepository.getAllProducts()

    private val _insertProductResult = MutableLiveData<Resource<Boolean>>()
    private val _updateProductResult = MutableLiveData<Resource<Boolean>>()
    private val _deleteProductResult = MutableLiveData<Resource<Boolean>>()
    private val _searchInProductResult = MutableLiveData<Resource<List<Product>>>()

    val insertProductResult: LiveData<Resource<Boolean>> get() = _insertProductResult
    val updateProductResult: LiveData<Resource<Boolean>> get() = _updateProductResult
    val deleteProductResult: LiveData<Resource<Boolean>> get() = _deleteProductResult
    val searchInProductResult: LiveData<Resource<List<Product>>> get() = _searchInProductResult

    fun insertFile(product: Product) {
        val insertJob = viewModelScope.launch {
            if (!adminRepository.searchForExistingProduct(product.productTitle)) {
                _insertProductResult.postValue(Resource.Loading())
                adminRepository.insertProduct(product)
                _insertProductResult.postValue(Resource.Success(true,
                    "Product created successfully!"))
            } else {
                _insertProductResult.postValue(Resource.Error(null,
                    "This product has been created before!"))
            }
        }
        insertJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _insertProductResult.postValue(Resource.Error(null, "Unexpected error occurred!"))
            }
        }
    }

    fun updateFile(product: Product) {
        val updateJob = viewModelScope.launch {
            _updateProductResult.postValue(Resource.Loading())
            adminRepository.updateProduct(product)
            _updateProductResult.postValue(Resource.Success(true,
                "Updating product is successfully!"))
        }
        updateJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _updateProductResult.postValue(Resource.Error(null, "Unexpected error occurred!"))
            }
        }
    }

    fun deleteFile(product: Product) {
        val deleteJob = viewModelScope.launch {
            _deleteProductResult.postValue(Resource.Loading())
            adminRepository.deleteProduct(product)
            _deleteProductResult.postValue(Resource.Success(true,
                "Deleting product is successfully!"))
        }
        deleteJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _deleteProductResult.postValue(Resource.Error(null, "Unexpected error occurred!"))
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
}