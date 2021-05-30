package com.abproject

import android.app.Application
import com.abproject.tsp_cart.model.repository.AuthRepository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TSPApplication : Application() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        loadApplicationData()
    }


    private fun loadApplicationData() {
        authRepository.loadApplicationDataFromSharedPrefs()
    }
}