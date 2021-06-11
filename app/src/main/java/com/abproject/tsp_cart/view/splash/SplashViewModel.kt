package com.abproject.tsp_cart.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abproject.tsp_cart.base.TSPViewModel
import com.abproject.tsp_cart.model.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : TSPViewModel() {

    private val _isDataExisting = MutableLiveData<Boolean>()
    private val _isAdminPanel = MutableLiveData<Boolean>()

    val isDataExisting: LiveData<Boolean> get() = _isDataExisting
    val isAdminPanel: LiveData<Boolean> get() = _isAdminPanel

    fun checkData() {
        if (authRepository.checkDataIsExist()) {
            _isDataExisting.postValue(true)
            if (authRepository.checkUserDataIsAdmin()) {
                _isAdminPanel.postValue(true)
            } else
                _isAdminPanel.postValue(false)
        } else
            _isDataExisting.postValue(false)
    }
}