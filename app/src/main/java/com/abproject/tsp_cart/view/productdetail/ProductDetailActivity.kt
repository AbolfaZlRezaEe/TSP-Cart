package com.abproject.tsp_cart.view.productdetail

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityProductDetailBinding
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_ADMIN_DETAIL
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_USER_DETAIL
import com.abproject.tsp_cart.util.loadImage

class ProductDetailActivity : TSPActivity() {

    private val productDetailViewModel: ProductDetailViewModel by viewModels()
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
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
                    productDetailViewModel.getUserName(),
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
            binding.productPriceDetail.text = adminProduct.productPrice
            binding.productDiscountedPriceDetail.text = adminProduct.discountedProductPrice
            binding.productInventoryDetail.text = adminProduct.productInventory.toString()
            binding.productSoldDetail.text = adminProduct.productSold.toString()
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
            binding.productPriceDetail.text = userProduct.productPrice
            binding.productDiscountedPriceDetail.text = userProduct.discountedProductPrice
            binding.productInventoryDetail.text = userProduct.productInventory.toString()
            binding.productSoldDetail.text = userProduct.productSold.toString()
        }
    }
}