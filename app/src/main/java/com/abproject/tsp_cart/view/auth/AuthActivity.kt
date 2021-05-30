package com.abproject.tsp_cart.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.abproject.tsp_cart.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initializeNavHost()
    }

    private fun initializeNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.authenticationNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
    }
}