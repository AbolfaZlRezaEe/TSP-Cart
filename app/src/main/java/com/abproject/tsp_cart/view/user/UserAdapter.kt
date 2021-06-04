package com.abproject.tsp_cart.view.user

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

class UserAdapter @Inject constructor(
    private val productItemClickListener: ProductItemClickListener,
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

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

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.imageItemProduct)
        private val productTitle: TextView = itemView.findViewById(R.id.productTitleItemProduct)
        private val productPrice: TextView = itemView.findViewById(R.id.productPriceItemProduct)
        private val productButton: ImageView =
            itemView.findViewById(R.id.buttonProductItemProduct)

        fun bindProduct(product: Product) {
            productTitle.text = product.productTitle
            productImage.loadImage(Uri.parse(product.thumbnailPicture))
            productPrice.text = "$${product.productPrice}"
            productButton.setImageResource(R.drawable.ic_buy)
            productButton.setOnClickListener {
                productItemClickListener.onBuy(product)
            }
            itemView.setOnClickListener {
                productItemClickListener.onClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindProduct(products[position])
    }

    override fun getItemCount(): Int = products.size

    interface ProductItemClickListener {
        fun onBuy(product: Product)
        fun onClick(product: Product)
    }
}