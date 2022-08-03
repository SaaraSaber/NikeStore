package com.example.nikestore.feature.main.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ItemFavoriteProductBinding
import com.example.nikestore.services.http.ImageLoadingService

class FavoriteProductsAdapter(
    val products: MutableList<Product>,
    val favoriteProductsEventListener: FavoriteProductsEventListener,
    val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<FavoriteProductsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemFavoriteProductBinding.bind(view)

        fun bind(product: Product) {
            binding.productTitleTv.text = product.title
            imageLoadingService.load(binding.nikeImageView, product.image)
            itemView.setOnClickListener {
                favoriteProductsEventListener.onClick(product)
            }
            itemView.setOnLongClickListener {
                products.remove(product)
                notifyItemRemoved(adapterPosition)
                favoriteProductsEventListener.onLongClick(product)
                return@setOnLongClickListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    interface FavoriteProductsEventListener {
        fun onClick(product: Product)
        fun onLongClick(product: Product)
    }
}