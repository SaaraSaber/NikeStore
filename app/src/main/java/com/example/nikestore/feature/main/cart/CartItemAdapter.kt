package com.example.nikestore.feature.main.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.CartItem
import com.example.nikestore.data.PurchaseDetail
import com.example.nikestore.databinding.ItemCartBinding
import com.example.nikestore.databinding.ItemPurchaseDetailsBinding
import com.example.nikestore.services.http.ImageLoadingService
import com.example.nikestore.view.NikeImageView

const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAIL = 1

class CartItemAdapter(
    val cartItems: MutableList<CartItem>,
    val imageLoadingService: ImageLoadingService,
    val cartItemViewCallbacks: CartItemViewCallbacks
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var purchaseDetail: PurchaseDetail? = null

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCartBinding.bind(itemView)

        fun bindCartItem(cartItem: CartItem) {
            binding.productTitleTv.text = cartItem.product.title
            binding.cartItemCountTv.text = cartItem.count.toString()
            binding.previousPriceTv.text =
                formatPrice(cartItem.product.price + cartItem.product.discount)
            binding.priceTv.text = formatPrice(cartItem.product.price)
            imageLoadingService.load(binding.productIv, cartItem.product.image)
            binding.removeFromCartBtn.setOnClickListener {
                cartItemViewCallbacks.onRemoveCartItemButtonClick(cartItem)
            }

            binding.changeCountProgressBar.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.VISIBLE else View.GONE
            binding.cartItemCountTv.visibility =
                if (cartItem.changeCountProgressBarIsVisible) View.INVISIBLE else View.VISIBLE

            binding.increaseBtn.setOnClickListener {
                cartItem.changeCountProgressBarIsVisible = true
                binding.changeCountProgressBar.visibility = View.VISIBLE
                binding.cartItemCountTv.visibility = View.INVISIBLE
                cartItemViewCallbacks.onIncreaseCartItemButtonClick(cartItem)
            }
            binding.decreaseBtn.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.changeCountProgressBarIsVisible = true
                    binding.changeCountProgressBar.visibility = View.VISIBLE
                    binding.cartItemCountTv.visibility = View.INVISIBLE
                    cartItemViewCallbacks.onDecreaseCartItemButtonClick(cartItem)
                }
            }
            binding.productIv.setOnClickListener {
                cartItemViewCallbacks.onProductImageClick(cartItem)
            }
        }
    }

    class PurchaseDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemPurchaseDetailsBinding.bind(itemView)

        fun bind(totalPrice: Int, shippingCost: Int, payablePrice: Int) {
            binding.totalPriceTv.text = formatPrice(totalPrice)
            binding.shippingCostTv.text = formatPrice(shippingCost)
            binding.payablePriceTv.text = formatPrice(payablePrice)
        }
    }

    interface CartItemViewCallbacks {
        fun onRemoveCartItemButtonClick(cartItem: CartItem)
        fun onIncreaseCartItemButtonClick(cartItem: CartItem)
        fun onDecreaseCartItemButtonClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CART_ITEM)
            return CartItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
            )
        else return PurchaseDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_purchase_details, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder)
            holder.bindCartItem(cartItems[position])
        else if (holder is PurchaseDetailViewHolder)
            purchaseDetail?.let {
                holder.bind(it.total_price, it.shipping_cost, it.payable_price)
            }
    }

    override fun getItemCount(): Int = cartItems.size + 1

    override fun getItemViewType(position: Int): Int {
        if (position == cartItems.size)
            return VIEW_TYPE_PURCHASE_DETAIL
        else
            return VIEW_TYPE_CART_ITEM
    }

    fun removeCartItem(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun increaseAndDecreaseCount(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarIsVisible = false
            notifyItemChanged(index)
        }
    }

}