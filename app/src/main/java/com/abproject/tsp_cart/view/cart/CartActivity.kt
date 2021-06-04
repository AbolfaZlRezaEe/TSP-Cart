package com.abproject.tsp_cart.view.cart

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityCartBinding
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables
import com.abproject.tsp_cart.view.productdetail.ProductDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartActivity :
    TSPActivity(), CartAdapterCallBacks {

    @Inject
    lateinit var cartAdapter: CartAdapter

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonCart.setOnClickListener {
            this.finish()
        }
        cartViewModel.getAllProductsFromCart()

        getObservers()
    }

    private fun getObservers() {
        cartViewModel.getAllProductsFromCart.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { list ->
                        setVisibilityForEmptyState(false)
                        setupRecyclerView(list)
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
                is Resource.EmptyState -> {
                    showProgressBar(false)
                    setVisibilityForEmptyState(true)
                }
            }
        }

        cartViewModel.deleteCartStatus.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { isSuccess ->
                        if (isSuccess) {
                            showSnackBar(response.message!!)
                        }
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
            }
        }

        cartViewModel.updateCartStatus.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { isSuccess ->
                        if (isSuccess) {
                            response.message?.let {
                                showSnackBar(it)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
            }
        }

        cartViewModel.purchaseDetailStatus.observe(this) { result ->
            cartAdapter.purchaseDetail = result
            cartAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView(carts: List<Cart>) {
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        cartAdapter.productDataChange(carts)
        binding.recyclerViewCart.adapter = cartAdapter
    }

    private fun setVisibilityForEmptyState(visibility: Boolean) {
        if (visibility) {
            binding.emptyStateTextCart.visibility = View.VISIBLE
            binding.recyclerViewCart.visibility = View.INVISIBLE
        } else {
            binding.emptyStateTextCart.visibility = View.GONE
            binding.recyclerViewCart.visibility = View.VISIBLE
        }
    }

    override fun onRemoveItemFromCart(cart: Cart) {
        cartViewModel.deleteProductFromCart(cart)
    }

    override fun onIncreaseItemCart(cart: Cart) {
        cartViewModel.increaseCartItem(cart)
        cartAdapter.increaseAndDecreaseItem(cart)
    }

    override fun onDecreaseItemCart(cart: Cart) {
        cartViewModel.decreaseCartItem(cart)
        cartAdapter.increaseAndDecreaseItem(cart)
    }

    override fun onProductClick(cart: Cart) {
        val product = Product(
            productTitle = cart.productName,
            thumbnailPicture = cart.thumbnailPicture,
            productPictures = cart.productPictures,
            productPrice = cart.productPrice,
            productDiscountPrice = cart.productDiscountedPrice!!,
            productInventory = cart.productInventory,
            productSold = cart.productSold
        )
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(Variables.EXTRA_KEY_USER_DETAIL, product)
        })
    }

}