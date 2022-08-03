package com.example.nikestore.feature.main.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.NikeActivity
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ActivityFavoriteProductsBinding
import com.example.nikestore.feature.main.product.ProductDetailActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class FavoriteProductsActivity : NikeActivity(),
    FavoriteProductsAdapter.FavoriteProductsEventListener {
    private lateinit var binding: ActivityFavoriteProductsBinding
    val viewModel: FavoriteViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.helpBtn.setOnClickListener {
            Snackbar.make(it,R.string.favorites_help_message,Snackbar.LENGTH_SHORT).show()
        }

        viewModel.productLiveData.observe(this) {
            if (it.isNotEmpty()) {
                binding.favoriteProductsRv.layoutManager =
                    LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                binding.favoriteProductsRv.adapter =
                    FavoriteProductsAdapter(it as MutableList<Product>, this, get())
            } else {
                showEmptyState(R.layout.view_default_empty_state)
                val emptyStateMessageTv = findViewById<TextView>(R.id.emptyStateMessageTv)
                emptyStateMessageTv.text = getString(R.string.favorites_empty_state_message)
            }
        }
    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onLongClick(product: Product) {
        viewModel.removeFromFavorite(product)
    }
}