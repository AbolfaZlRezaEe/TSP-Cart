package com.abproject.tsp_cart.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.databinding.ActivitySplashBinding
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_ADMIN_LOGIN
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_USER_LOGIN
import com.abproject.tsp_cart.view.admin.AdminActivity
import com.abproject.tsp_cart.view.auth.AuthActivity
import com.abproject.tsp_cart.view.user.UserActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleSplashScreen()
    }

    private fun isAdmin(): Boolean {
        val adminArgument = intent.extras?.getBoolean(EXTRA_KEY_ADMIN_LOGIN)
        return adminArgument != null && adminArgument == true
    }

    private fun isUser(): Boolean {
        val userArgument = intent.extras?.getBoolean(EXTRA_KEY_USER_LOGIN)
        return userArgument != null && userArgument == true
    }

    private fun handleCartText() {
        when {
            isAdmin() -> {
                binding.cartTextSplash.text = getString(R.string.admin)
            }
            isUser() -> {
                binding.cartTextSplash.text = getString(R.string.user)
            }
            else -> {
                binding.cartTextSplash.text = getString(R.string.cart)
            }
        }
    }

    private fun handleSplashScreen() {
        handleCartText()
        Handler(Looper.getMainLooper()).postDelayed({

            when {
                isAdmin() -> {
                    val intent = Intent(this, AdminActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()

                }
                isUser() -> {
                    val intent = Intent(this, UserActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()

                }
                else -> {
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                }
            }

        }, 2000)
    }
}