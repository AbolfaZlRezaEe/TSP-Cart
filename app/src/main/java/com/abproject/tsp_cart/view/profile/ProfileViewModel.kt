package com.abproject.tsp_cart.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.model.repository.auth.AuthRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : TSPViewModel() {

    private val _getUserInformation = MutableLiveData<Resource<User>>()
    private val _updateUserInformation = MutableLiveData<Resource<Boolean>>()

    val getUserInformation: LiveData<Resource<User>> get() = _getUserInformation
    val updateUserInformation: LiveData<Resource<Boolean>> get() = _updateUserInformation

    init {
        getUserInformation()
    }

    fun getUserInformation() {
        val getUserInformation = viewModelScope.launch {
            _getUserInformation.postValue(Resource.Loading())
            val result =
                authRepository.getUserInformation()
            result?.let { user ->
                _getUserInformation.postValue(Resource.Success(user, null))
            }
            if (result == null)
                _getUserInformation.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
        }
        getUserInformation.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                Timber.e(it)
                _getUserInformation.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun updateUser(
        user: User,
    ) {
        val updateUserInformation = viewModelScope.launch {
            _updateUserInformation.postValue(Resource.Loading())
            authRepository.updateUser(user)
            _updateUserInformation.postValue(Resource.Success(
                true,
                "User Information Update Successful!"
            ))
        }
        updateUserInformation.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                Timber.e(it)
                _updateUserInformation.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

}