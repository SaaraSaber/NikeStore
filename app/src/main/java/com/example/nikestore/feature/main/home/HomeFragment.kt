package com.example.nikestore.feature.main.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.NikeFragment
import com.example.nikestore.common.EXTRA_KEY_DATA
import com.example.nikestore.common.convertDpToPixel
import com.example.nikestore.data.Product
import com.example.nikestore.data.SORT_LATEST
import com.example.nikestore.databinding.FragmentHomeBinding
import com.example.nikestore.feature.main.BannerSliderAdapter
import com.example.nikestore.feature.main.common.ProductListAdapter
import com.example.nikestore.feature.main.common.VIEW_TYPE_ROUND
import com.example.nikestore.feature.main.list.ProductListActivity
import com.example.nikestore.feature.main.product.ProductDetailActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class HomeFragment : NikeFragment(), ProductListAdapter.ProductEventListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val homeViewModel: HomeViewModel by viewModel()
    val productListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    val productPopularListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.latestProductRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.latestProductRv.adapter = productListAdapter
        productListAdapter.productEventListener = this

        binding.popularProductRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.popularProductRv.adapter = productPopularListAdapter
        productPopularListAdapter.productEventListener = this

        binding.viewLatestProductsBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA, SORT_LATEST)
            })
        }

        homeViewModel.productLiveDataLatest.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            productListAdapter.products = it as ArrayList<Product>
        }

        homeViewModel.productLiveDataPopular.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            productPopularListAdapter.products = it as ArrayList<Product>
        }

        homeViewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        homeViewModel.bannerLiveData.observe(viewLifecycleOwner) {
            Timber.i(it.toString())
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            binding.bannerSliderViewPager.adapter = bannerSliderAdapter

            val viewPagerHeight = (((binding.bannerSliderViewPager.measuredWidth - convertDpToPixel(
                32f,
                requireContext()
            )) * 173) / 328).toInt()
            val layoutParams = binding.bannerSliderViewPager.layoutParams
            layoutParams.height = viewPagerHeight
            binding.bannerSliderViewPager.layoutParams = layoutParams
            binding.sliderIndicator.attachTo(binding.bannerSliderViewPager)
        }

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                homeViewModel.search(p0.toString())
                homeViewModel.productLiveData.observe(viewLifecycleOwner) {
                    Timber.i(it.toString())
                    if (it.isEmpty()) {
                        productListAdapter.products =
                            homeViewModel.productLiveDataLatest.value as ArrayList<Product>
                        Toast.makeText(requireContext(), "محصولی پیدا نشد", Toast.LENGTH_SHORT)
                            .show()
                    } else productListAdapter.products = it as ArrayList<Product>
                }
            }
        })

    }


    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        homeViewModel.addProductToFavorite(product)
    }
}