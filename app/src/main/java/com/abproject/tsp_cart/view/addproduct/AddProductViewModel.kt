package com.abproject.tsp_cart.view.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.repository.admin.AdminRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
) : TSPViewModel() {

    private val _insertProductResult = MutableLiveData<Resource<Boolean>>()
    private val _updateProductResult = MutableLiveData<Resource<Boolean>>()
    private val _deleteProductResult = MutableLiveData<Resource<Boolean>>()

    val insertProductResult: LiveData<Resource<Boolean>> get() = _insertProductResult
    val updateProductResult: LiveData<Resource<Boolean>> get() = _updateProductResult
    val deleteProductResult: LiveData<Resource<Boolean>> get() = _deleteProductResult

    fun insertProduct(product: Product) {
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

    fun deleteProduct(product: Product) {
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

    fun updateProduct(product: Product) {
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

}