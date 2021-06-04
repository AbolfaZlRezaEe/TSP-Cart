package com.abproject.tsp_cart.view.cart

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abproject.tsp_cart.R
import com.abproject.tsp_cart.model.dataclass.Cart
import com.abproject.tsp_cart.model.dataclass.PurchaseDetail
import com.abproject.tsp_cart.util.Variables.VIEW_TYPE_CART
import com.abproject.tsp_cart.util.Variables.VIEW_TYPE_PURCHASE
import com.abproject.tsp_cart.util.loadImage
import javax.inject.Inject

class CartAdapter @Inject constructor(
    private val cartItemClickListener: CartAdapterCallBacks,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val carts: MutableList<Cart> = mutableListOf()
    var purchaseDetail: PurchaseDetail? = null

    fun productDataChange(carts: List<Cart>) {
        this.carts.clear()
        this.carts.addAll(carts)
        notifyDataSetChanged()
    }

    fun onRemoveCart(cart: Cart) {
        val index = carts.indexOf(cart)
        if (index > -1) {
            carts.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun increaseAndDecreaseItem(cart: Cart) {
        val index = carts.indexOf(cart)
        if (index > -1) {
            carts[index].progressBarVisible = false
            notifyItemChanged(index)
        }
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productTitle: TextView = itemView.findViewById(R.id.productTitleItemProductCart)
        private val productPrice: TextView = itemView.findViewById(R.id.productPriceItemProductCart)
        private val productAmount: TextView =
            itemView.findViewById(R.id.productAmountItemProductCart)
        private val productThumbnail: ImageView = itemView.findViewById(R.id.imageItemProductCart)
        private val increaseItem: ImageView =
            itemView.findViewById(R.id.increaseProductAmountItemProductCart)
        private val decreaseItem: ImageView =
            itemView.findViewById(R.id.decreaseProductAmountItemProductCart)
        private val progressBar: ProgressBar =
            itemView.findViewById(R.id.progressBarItemProductCart)

        fun bindCart(cart: Cart) {
            productTitle.text = cart.productName
            productPrice.text = "$${cart.productPrice}"
            productAmount.text = cart.amount.toString()
            productThumbnail.loadImage(Uri.parse(cart.thumbnailPicture))

            progressBar.visibility =
                if (cart.progressBarVisible) View.VISIBLE else View.INVISIBLE
            productAmount.visibility =
                if (cart.progressBarVisible) View.INVISIBLE else View.VISIBLE

            itemView.setOnLongClickListener {
                cartItemClickListener.onRemoveItemFromCart(cart)
                true
            }
            itemView.setOnClickListener {
                cartItemClickListener.onProductClick(cart)
            }

            increaseItem.setOnClickListener {
                cart.progressBarVisible = true
                productAmount.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                cartItemClickListener.onIncreaseItemCart(
                    cart
                )
            }

            decreaseItem.setOnClickListener {
                    cart.progressBarVisible = true
                    progressBar.visibility = View.VISIBLE
                    productAmount.visibility = View.INVISIBLE
                    cartItemClickListener.onDecreaseItemCart(
                        cart
                    )
                }
        }
    }

    inner class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val totalPriceItem: TextView = itemView.findViewById(R.id.totalPriceItemPurchase)
        private val discountPriceItem: TextView =
            itemView.findViewById(R.id.discountPriceItemPurchase)
        private val payablePriceItem: TextView =
            itemView.findViewById(R.id.payablePriceItemPurchase)

        fun bindPurchase(
            totalPrice: Long,
            discountPrice: Long,
            payablePrice: Long,
        ) {
            totalPriceItem.text = "$${totalPrice}"
            discountPriceItem.text = "$${discountPrice}"
            payablePriceItem.text = "$${payablePrice}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CART) {
            CartViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_cart, parent, false)
            )
        } else {
            PurchaseViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_purchase, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartViewHolder) {
            holder.bindCart(carts[position])
        } else if (holder is PurchaseViewHolder) {
            purchaseDetail?.let { response ->
                holder.bindPurchase(
                    totalPrice = response.totalPrice,
                    discountPrice = response.discountPrice,
                    payablePrice = response.payablePrice
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == carts.size)
            VIEW_TYPE_PURCHASE
        else
            VIEW_TYPE_CART
    }

    override fun getItemCount(): Int {
        return carts.size + 1
    }
}