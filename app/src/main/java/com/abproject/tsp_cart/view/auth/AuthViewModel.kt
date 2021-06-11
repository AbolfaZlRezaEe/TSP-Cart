package com.abproject.tsp_cart.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.model.repository.auth.AuthRepository
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : TSPViewModel() {

    private val _saveUserInformationResult = MutableLiveData<Resource<Boolean>>()
    private val _checkUserInformationStatus = MutableLiveData<LiveData<Resource<Boolean>>>()

    val saveUserInformationResult: LiveData<Resource<Boolean>> get() = _saveUserInformationResult
    val checkUserInformationStatus: LiveData<LiveData<Resource<Boolean>>> get() = _checkUserInformationStatus

    fun saveUserInformation(
        fullName: String,
        email: String,
        username: String,
        password: String,
        isAdmin: Boolean,
        createAt: String,
    ) {
        val user = User(
            fullname = fullName,
            email = email,
            username = username,
            password = password,
            isManager = isAdmin,
            createdAt = createAt
        )
        val insertJob = viewModelScope.launch {
            _saveUserInformationResult.postValue(Resource.Loading())
            val result = authRepository.insertUser(user)
            if (result)
                _saveUserInformationResult.postValue(Resource.Success(true, null))
            else
                _saveUserInformationResult.postValue(Resource.Error(null,
                    "This username has been created before!"))
        }

        insertJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _saveUserInformationResult.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }

    fun checkUserInformation(
        username: String,
        password: String,
        isAdmin: Boolean,
    ) {
        viewModelScope.launch {
            _checkUserInformationStatus.postValue(authRepository.checkUserExisting(
                username = username,
                password = password,
                isAdmin = isAdmin
            ))
        }
    }
}