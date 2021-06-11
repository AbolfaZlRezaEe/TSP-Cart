package com.abproject.tsp_cart.view.admin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityAdminBinding
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_ADMIN_DETAIL
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_EDIT_PRODUCT
import com.abproject.tsp_cart.view.addproduct.AddProductActivity
import com.abproject.tsp_cart.view.auth.AuthActivity
import com.abproject.tsp_cart.view.productdetail.ProductDetailActivity
import com.abproject.tsp_cart.view.profile.ProfileActivity
import com.abproject.tsp_cart.view.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminActivity : TSPActivity(),
    AdminAdapter.ProductItemClickListener {

    @Inject
    lateinit var adminAdapter: AdminAdapter
    private val adminViewModel: AdminViewModel by viewModels()
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getObservers()

        setupSearchInProduct()

        binding.addProductAdminHome.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        binding.logoutButtonAdmin.setOnClickListener {
            setupLogoutSection()
        }

        binding.profileMaterialCard.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        adminViewModel.getAllProduct()
        setupShowUserInformation()
    }

    private fun setupShowUserInformation() {
        val data = adminViewModel.getUsernameAndEmail()
        binding.usernameTextViewAdmin.text = data.first
        binding.emailTextViewAdmin.text = data.second
    }

    private fun setupLogoutSection() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.logoutDialogTitle))
            .setMessage(getString(R.string.logoutDialogMessage))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                startActivity(Intent(this, SplashActivity::class.java))
                dialog.dismiss()
                adminViewModel.logout()
                finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupSearchInProduct() {
        binding.searchInProductEditTextAdmin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { string ->
                    if (string.isNotEmpty()) {
                        adminViewModel.searchInProducts(string.toString())
                    } else
                        adminViewModel.getAllProduct()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getObservers() {
        adminViewModel.getAllProducts.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { products ->
                        setVisibilityForEmptyState(false)
                        setupRecyclerView(products)
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
                is Resource.EmptyState -> {
                    adminAdapter.clearData()
                    showProgressBar(false)
                    setVisibilityForEmptyState(true)
                }
            }
        }
        adminViewModel.searchInProductResult.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { products ->
                        setVisibilityForEmptyState(false)
                        setupRecyclerView(products)
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
                is Resource.EmptyState -> {
                    adminAdapter.clearData()
                    showProgressBar(false)
                    setVisibilityForEmptyState(true)
                }
            }
        }
    }

    private fun setupRecyclerView(list: List<Product>) {
        binding.recyclerViewAdminHomeActivity.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        adminAdapter.productDataChange(list)
        binding.recyclerViewAdminHomeActivity.adapter = adminAdapter
    }

    private fun setVisibilityForEmptyState(visibility: Boolean) {
        if (visibility)
            binding.emptyStateTextAdmin.visibility = View.VISIBLE
        else
            binding.emptyStateTextAdmin.visibility = View.GONE
    }

    override fun onEdit(product: Product) {
        startActivity(Intent(this, AddProductActivity::class.java).apply {
            putExtra(EXTRA_KEY_EDIT_PRODUCT, product)
        })
    }

    override fun onclick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_ADMIN_DETAIL, product)
        })
    }
}