package com.abproject.tsp_cart.view.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.model.repository.AuthRepository
import com.abproject.tsp_cart.util.EncryptionTools
import com.abproject.tsp_cart.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : TSPViewModel() {

    private val _saveUserInformationResult = MutableLiveData<Resource<Boolean>>()
    private val _userExistingResult = MutableLiveData<Resource<Boolean>>()

    val saveUserInformationResult: LiveData<Resource<Boolean>> get() = _saveUserInformationResult
    val userExistingResult: LiveData<Resource<Boolean>> get() = _userExistingResult

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
        context: Context,
    ) {
        val checkUserJob = viewModelScope.launch {
            _userExistingResult.postValue(Resource.Loading())
            val result = authRepository.searchInUsersByUsername(username)
            if (result != null) {
                val decryptedPassword = EncryptionTools(context).decryptRSA(result.password)
                if (decryptedPassword == password) {
                    _userExistingResult.postValue(Resource.Success(true, null))
                } else
                    _userExistingResult.postValue(Resource.Error(null,
                        "Password is incorrect!"))
            } else
                _userExistingResult.postValue(Resource.Error(null,
                    "Username isn't valid!"))
        }
        checkUserJob.invokeOnCompletion { throwable ->
            throwable?.message?.let {
                _userExistingResult.postValue(Resource.Error(null,
                    "Unexpected error occurred!"))
            }
        }
    }
}