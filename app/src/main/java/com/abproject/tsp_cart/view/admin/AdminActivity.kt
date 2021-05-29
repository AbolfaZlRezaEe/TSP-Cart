package com.abproject.tsp_cart.view.admin

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.base.TSPActivity
import com.abproject.tsp_cart.databinding.ActivityAdminBinding
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.Resource
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

        binding.addProductAdminHome.setOnClickListener {
            //TODO Go to the product creator
        }
    }

    private fun getObservers() {
        adminViewModel.getAllProducts.observe(this) { response ->
            if (response.isNullOrEmpty()) {
                setVisibilityForEmptyState(true)
            } else {
                setVisibilityForEmptyState(false)
                setupRecyclerView(response)
            }
        }
        adminViewModel.insertProductResult.observe(this) { response ->
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
        adminViewModel.updateProductResult.observe(this) { response ->
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
        adminViewModel.deleteProductResult.observe(this) { response ->
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
        TODO("Go to Product creator with edit bundle.")
    }
}