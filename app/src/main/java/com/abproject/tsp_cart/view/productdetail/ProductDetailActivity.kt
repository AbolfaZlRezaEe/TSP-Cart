package com.abproject.tsp_cart.view.productdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityProductDetailBinding
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.model.dataclass.UserData
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_ADMIN_DETAIL
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_USER_DETAIL
import com.abproject.tsp_cart.util.loadImage
import com.abproject.tsp_cart.view.cart.CartActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : TSPActivity() {

    private val productDetailViewModel: ProductDetailViewModel by viewModels()
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        getObservers()

    }

    private fun getObservers() {
        productDetailViewModel.deleteProductStatus.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { isSuccess ->
                        if (isSuccess) {
                            showSnackBar(response.message!!)
                            Handler(Looper.getMainLooper()).postDelayed({
                                this.finish()
                            }, 2000)
                        }
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
            }
        }

        productDetailViewModel.addToCartStatus.observe(this) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar(true)
                }
                is Resource.Success -> {
                    showProgressBar(false)
                    response.data?.let { isSuccess ->
                        if (isSuccess) {
                            showSnackBar(response.message!!)
                            Handler(Looper.getMainLooper()).postDelayed({
                                startActivity(Intent(this, CartActivity::class.java))
                            }, 2000)
                        }
                    }
                }
                is Resource.Error -> {
                    showProgressBar(false)
                    showSnackBar(response.message!!)
                }
            }
        }
    }

    private fun setupUi() {
        val resultFromAdminBundle = intent.extras?.getParcelable<Product>(EXTRA_KEY_ADMIN_DETAIL)
        val resultFromUserBundle = intent.extras?.getParcelable<Product>(EXTRA_KEY_USER_DETAIL)

        setUiForAdminPanel(resultFromAdminBundle)
        setUiForUserPanel(resultFromUserBundle)

        binding.deleteImageViewDetail.setOnClickListener {
            setupDeleteFunctionality(resultFromAdminBundle)
        }
        binding.addToCartFabDetail.setOnClickListener {
            setupAddToCartFunctionality(resultFromUserBundle)
        }
        binding.backImageViewDetail.setOnClickListener {
            this.finish()
        }
    }

    private fun setupDeleteFunctionality(
        adminProduct: Product?,
    ) {
        adminProduct?.let { product ->
            productDetailViewModel.deleteProduct(product)
        }
    }

    private fun setupAddToCartFunctionality(
        userProduct: Product?,
    ) {
        userProduct?.let { product ->
            productDetailViewModel.addToCart(
                convertProductToCartForSave(
                    product,
                    UserData.username!!,
                    1
                )
            )
        }
    }

    private fun setUiForAdminPanel(
        product: Product?,
    ) {
        product?.let { adminProduct ->
            binding.addToCartFabDetail.visibility = View.GONE
            binding.deleteImageViewDetail.visibility = View.VISIBLE
            binding.productThumbnailDetail.loadImage(Uri.parse(adminProduct.thumbnailPicture))
            binding.productTitleDetail.text = adminProduct.productTitle
            binding.productPriceDetail.text = "$${adminProduct.productPrice}"
            binding.productDiscountedPriceDetail.text = "$${adminProduct.productDiscountPrice}"
            binding.productInventoryDetail.text = adminProduct.productInventory.toString()
            binding.productSoldDetail.text = adminProduct.productSold.toString()
            binding.usernameProductOwnerProductDetail.text = product.productOwner
            binding.emailProductOwnerProductDetail.text = product.productOwnerEmail
            setupProductOwnerProfile(
                username = product.productOwner,
                email = product.productOwnerEmail,
                profile = null
            )
        }
    }

    private fun setUiForUserPanel(
        product: Product?,
    ) {
        product?.let { userProduct ->
            binding.addToCartFabDetail.visibility = View.VISIBLE
            binding.deleteImageViewDetail.visibility = View.GONE
            binding.productThumbnailDetail.loadImage(Uri.parse(userProduct.thumbnailPicture))
            binding.productTitleDetail.text = userProduct.productTitle
            binding.productPriceDetail.text = "$${userProduct.productPrice}"
            binding.productDiscountedPriceDetail.text = "$${userProduct.productDiscountPrice}"
            binding.productInventoryDetail.text = userProduct.productInventory.toString()
            binding.productSoldDetail.text = userProduct.productSold.toString()
            binding.usernameProductOwnerProductDetail.text = product.productOwner
            binding.emailProductOwnerProductDetail.text = product.productOwnerEmail
            setupProductOwnerProfile(
                username = product.productOwner,
                email = product.productOwnerEmail,
                profile = null
            )
        }
    }

    private fun setupProductOwnerProfile(
        username: String,
        email: String,
        profile: String?,
    ) {
        if (profile != null) {
            showFirstCharForProductOwnerProfile(false)
            Glide.with(this)
                .load(Uri.parse(profile))
                .into(binding.profileImageViewProductDetail)
        } else {
            showFirstCharForProductOwnerProfile(true)
            binding.firstCharUserNameProductDetail.text =
                username.first().toString()
        }
        binding.emailProductOwnerProductDetail.text = email
    }

    private fun showFirstCharForProductOwnerProfile(
        showFirstChar: Boolean,
    ) {
        if (showFirstChar) {
            binding.firstCharUserNameProductDetail.visibility = View.VISIBLE
            binding.profileImageViewProductDetail.visibility = View.GONE
        } else {
            binding.firstCharUserNameProductDetail.visibility = View.GONE
            binding.profileImageViewProductDetail.visibility = View.VISIBLE
        }
    }
}