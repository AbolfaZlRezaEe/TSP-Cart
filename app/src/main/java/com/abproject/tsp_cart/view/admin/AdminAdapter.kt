package com.abproject.tsp_cart.view.admin

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.model.dataclass.Product
import com.abproject.tsp_cart.util.loadImage
import javax.inject.Inject

class AdminAdapter @Inject constructor(
    private val productItemClickListener: ProductItemClickListener,
) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {


    private val products: MutableList<Product> = mutableListOf()

    fun productDataChange(product: List<Product>) {
        this.products.clear()
        this.products.addAll(product)
        notifyDataSetChanged()
    }

    fun addProduct(product: Product) {
        this.products.add(0, product)
        notifyItemInserted(0)
    }

    fun clearData() {
        products.clear()
        notifyDataSetChanged()
    }

    inner class AdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.imageItemProduct)
        private val productTitle: TextView = itemView.findViewById(R.id.productTitleItemProduct)
        private val productPrice: TextView = itemView.findViewById(R.id.productPriceItemProduct)
        private val productEditButton: ImageView =
            itemView.findViewById(R.id.buttonProductItemProduct)

        fun bindProduct(product: Product) {
            productTitle.text = product.productTitle
            productImage.loadImage(Uri.parse(product.thumbnailPicture))
            productPrice.text = "$${product.productPrice}"
            productEditButton.setOnClickListener {
                productItemClickListener.onEdit(product)
            }
            itemView.setOnClickListener {
                productItemClickListener.onclick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        return AdminViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        holder.bindProduct(products[position])
    }

    override fun getItemCount(): Int = products.size

    interface ProductItemClickListener {
        fun onEdit(product: Product)
        fun onclick(product: Product)
    }
}