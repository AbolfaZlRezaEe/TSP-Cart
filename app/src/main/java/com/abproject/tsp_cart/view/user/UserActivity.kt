package com.abproject.tsp_cart.view.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityUserBinding
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_USER_DETAIL
import com.abproject.tsp_cart.util.totalPriceGenerator
import com.abproject.tsp_cart.view.productdetail.ProductDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserActivity : TSPActivity(),
    UserAdapter.ProductItemClickListener {

    @Inject
    lateinit var userAdapter: UserAdapter
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.usernameTextUser.text = userViewModel.getUserName()
        binding.goToCartButtonUser.setOnClickListener {

        }
        getObservers()
    }

    private fun getObservers() {
        userViewModel.getAllProducts.observe(this) { response ->
            if (response.isNullOrEmpty()) {
                setVisibilityForEmptyState(true)
            } else {
                setVisibilityForEmptyState(false)
                setupRecyclerView(response)
            }

        }
        userViewModel.addToCartStatus.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { isSuccess ->
                        if (isSuccess)
                            showSnackBar(response.message!!)

                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
            }
        }
    }

    private fun setupRecyclerView(list: List<Product>) {
        binding.recyclerViewUser.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        userAdapter.productDataChange(list)
        binding.recyclerViewUser.adapter = userAdapter
    }

    private fun setVisibilityForEmptyState(visibility: Boolean) {
        if (visibility)
            binding.emptyStateTextUser.visibility = View.VISIBLE
        else
            binding.emptyStateTextUser.visibility = View.GONE
    }

    override fun onBuy(product: Product) {
        val cart = Cart(
            productName = product.productTitle,
            userName = userViewModel.getUserName(),
            thumbnailPicture = product.thumbnailPicture,
            productPictures = product.productPictures,
            amount = 1,
            productPrice = returnProductPrice(
                product.productPrice,
                product.discountedProductPrice
            ),
            totalPrice = totalPriceGenerator(
                1,
                product.productPrice
            ) ?: ""
        )
        userViewModel.addToCart(cart)
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_USER_DETAIL, product)
        })
    }

    private fun returnProductPrice(
        productPrice: String,
        discountedProductPrice: String,
    ): String {
        return if (discountedProductPrice.isNotEmpty())
            discountedProductPrice
        else
            productPrice
    }
}