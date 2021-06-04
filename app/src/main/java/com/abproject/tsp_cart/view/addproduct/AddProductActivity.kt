package com.abproject.tsp_cart.view.addproduct

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityAddProductBinding
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Resource
import com.abproject.tsp_cart.util.Variables.EXTRA_KEY_EDIT_PRODUCT
import com.abproject.tsp_cart.util.Variables.REQUEST_CODE_CHOOSE_PRODUCT_PICTURES
import com.abproject.tsp_cart.util.Variables.REQUEST_CODE_CHOOSE_THUMBNAIL_PICTURE
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductActivity : TSPActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private val addProductViewModel: AddProductViewModel by viewModels()
    private var thumbnailUri: String? = null
    private var productPictures: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val argument = intent.extras?.getParcelable<Product>(EXTRA_KEY_EDIT_PRODUCT)
        setupUiForEditProduct(argument)

        getObservers()

        initializePriceEditTexts()

        binding.backButtonAddProduct.setOnClickListener {
            finish()
        }

        binding.deleteButtonAddProduct.setOnClickListener {
            addProductViewModel.deleteProduct(argument!!)
        }

        binding.addProductFab.setOnClickListener {
            setupAddOrEditProduct(argument)
        }

        binding.chooseProductThumbnail.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .start(REQUEST_CODE_CHOOSE_THUMBNAIL_PICTURE)
        }

        binding.chooseProductPicturesButton.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .start(REQUEST_CODE_CHOOSE_PRODUCT_PICTURES)
        }
    }

    private fun initializePriceEditTexts() {
        binding.productPriceEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.toString()?.startsWith("$")!!) {
                    binding.productPriceEditText.setText("$$s")
                    Selection.setSelection(
                        binding.productPriceEditText.text,
                        binding.productPriceEditText.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.productDiscountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.toString()?.startsWith("$")!!) {
                    binding.productDiscountEditText.setText("$$s")
                    Selection.setSelection(
                        binding.productDiscountEditText.text,
                        binding.productDiscountEditText.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkValidationOfEditTexts(): Boolean {
        return binding.productNameEditText.text.isNotEmpty()
                && binding.productPriceEditText.text.length > 1
                && binding.productDiscountEditText.text.length > 1
                && binding.productInventoryEditText.text.isNotEmpty()
    }

    private fun setErrorForEditTexts() {
        if (binding.productNameEditText.text.isEmpty())
            binding.productNameEditText.error = getString(R.string.productTitleError)
        if (binding.productPriceEditText.text.length == 1 || binding.productPriceEditText.text.isEmpty())
            binding.productPriceEditText.error = getString(R.string.productPriceError)
        if (binding.productDiscountEditText.text.length == 1 || binding.productDiscountEditText.text.isEmpty())
            binding.productDiscountEditText.error = getString(R.string.productDiscountPriceError)
        if (binding.productInventoryEditText.text.isEmpty())
            binding.productInventoryEditText.error = getString(R.string.productInventoryError)
    }

    private fun getObservers() {
        addProductViewModel.insertProductResult.observe(this) { response ->
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
        addProductViewModel.updateProductResult.observe(this) { response ->
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
        addProductViewModel.deleteProductResult.observe(this) { response ->
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
    }

    private fun setupUiForEditProduct(
        product: Product?,
    ) {
        product?.let { editProduct ->
            binding.deleteButtonAddProduct.visibility = View.VISIBLE
            binding.productTitleTextView.text = getString(R.string.editProduct)
            binding.productNameEditText.setText(editProduct.productTitle)
            binding.productPriceEditText.setText("$${editProduct.productPrice}")
            binding.productDiscountEditText.setText("$${editProduct.productDiscountPrice}")
            binding.productInventoryEditText.setText(editProduct.productInventory.toString())
        }
    }

    private fun setupAddOrEditProduct(
        product: Product?,
    ) {
        if (checkValidationOfEditTexts()) {
            val productPrice =
                binding.productPriceEditText.text.removePrefix("$").toString().toLong()
            val productDiscountPrice =
                binding.productDiscountEditText.text.removePrefix("$").toString().toLong()
            if (product != null) {
                if (!nothingChange(product)) {
                    product.productTitle = binding.productNameEditText.text.toString()
                    product.productPrice = productPrice
                    product.productDiscountPrice = productDiscountPrice
                    product.productInventory =
                        binding.productInventoryEditText.text.toString().toLong()
                    product.thumbnailPicture = thumbnailUri ?: product.thumbnailPicture
                    product.productPictures = productPictures ?: product.productPictures
                    addProductViewModel.updateProduct(product)
                } else {
                    showSnackBar("Please change something or exit in this page!")
                }
            } else {
                val product = Product(
                    productTitle = binding.productNameEditText.text.toString(),
                    productPrice = productPrice,
                    productDiscountPrice = productDiscountPrice,
                    thumbnailPicture = thumbnailUri ?: "",
                    productPictures = productPictures ?: arrayListOf(),
                    productInventory = binding.productInventoryEditText.text.toString().toLong(),
                    productSold = 0
                )
                addProductViewModel.insertProduct(product)
            }
        } else
            setErrorForEditTexts()
    }

    private fun nothingChange(
        product: Product,
    ): Boolean {
        return (product.productTitle == binding.productNameEditText.text.toString()
                && product.productPrice == binding.productPriceEditText.text
            .removePrefix("$").toString().toLong()
                && product.productDiscountPrice == binding.productDiscountEditText.text
            .removePrefix("$").toString().toLong()
                && product.productInventory ==
                binding.productInventoryEditText.text.toString().toLong()
                && product.thumbnailPicture == thumbnailUri ?: product.thumbnailPicture
                && product.productPictures == productPictures ?: product.productPictures)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_THUMBNAIL_PICTURE) {
                thumbnailUri = data?.data!!.toString()
            }

            if (requestCode == REQUEST_CODE_CHOOSE_PRODUCT_PICTURES) {
                productPictures = arrayListOf(
                    data?.data!!.toString()
                )
            }
        }
    }

}