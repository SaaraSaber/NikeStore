package com.example.nikestore.feature.main.list


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nikestore.NikeActivity
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.data.Product
import com.example.nikestore.databinding.ActivityProductListBinding
import com.example.nikestore.feature.main.common.ProductListAdapter
import com.example.nikestore.feature.main.common.VIEW_TYPE_LARGE
import com.example.nikestore.feature.main.common.VIEW_TYPE_SMALL
import com.example.nikestore.feature.main.home.HomeViewModel
import com.example.nikestore.feature.main.product.ProductDetailActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductListActivity : NikeActivity(), ProductListAdapter.ProductEventListener {
    private lateinit var binding: ActivityProductListBinding

    val viewModel: ProductListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_DATA
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.productsRv.layoutManager = gridLayoutManager
        binding.productsRv.adapter = adapter
        adapter.productEventListener = this

        binding.viewTypeChangerBtn.setOnClickListener {
            if (adapter.viewType == VIEW_TYPE_SMALL) {
                binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_view_type_large_24)
                adapter.viewType = VIEW_TYPE_LARGE
                gridLayoutManager.spanCount = 1
                adapter.notifyDataSetChanged()
            } else {
                binding.viewTypeChangerBtn.setImageResource(R.drawable.ic_baseline_grid24)
                adapter.viewType = VIEW_TYPE_SMALL
                gridLayoutManager.spanCount = 2
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        viewModel.productLiveData.observe(this) {
            Timber.i(it.toString())
            adapter.products = it as ArrayList<Product>
        }

        viewModel.selectedSortTitleLiveData.observe(this) {
            binding.selectedSortTitleTv.text = getString(it)
        }

        binding.sortBtn.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this).setSingleChoiceItems(
                R.array.sortTitleArray,
                viewModel.sort
            ) { dialog, selectedSoryIndex ->
                dialog.dismiss()
                viewModel.onSelectedSortChangeByUser(selectedSoryIndex)

            }.setTitle(getString(R.string.sort))
            dialog.show()
        }
        binding.toolbarView.onBackButtonClickListener = View.OnClickListener { finish() }
    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        viewModel.addProductToFavorite(product)
    }
}