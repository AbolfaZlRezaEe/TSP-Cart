package com.abproject.tsp_cart.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.databinding.ActivitySplashBinding
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_ADMIN_LOGIN
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_USER_LOGIN
import com.abproject.tsp_cart.view.admin.AdminActivity
import com.abproject.tsp_cart.view.auth.AuthActivity
import com.abproject.tsp_cart.view.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleSplashScreen()
        getObservers()
    }

    private fun getObservers() {
        splashViewModel.isAdminPanel.observe(this) { isAdmin ->
            if (isAdmin) {
                binding.cartTextSplash.text = getString(R.string.admin)
                val intent = Intent(this, AdminActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
            } else {
                binding.cartTextSplash.text = getString(R.string.user)
                val intent = Intent(this, UserActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
            }
        }
        splashViewModel.isDataExisting.observe(this) { isExist ->
            if (!isExist) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
            }
        }
    }

    private fun handleSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.checkData()
        }, 2000)
    }
}