package com.example.nikestore.feature.main.list

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.NikeViewModel
import com.example.nikestore.R
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.ProductRepository
import com.example.nikestore.feature.main.common.NikeCompletableObserver
import io.reactivex.schedulers.Schedulers

class ProductListViewModel(var sort: Int, val productRepository: ProductRepository) :
    NikeViewModel() {
    val productLiveData = MutableLiveData<List<Product>>()
    val selectedSortTitleLiveData = MutableLiveData<Int>()
    val sortTitles = arrayOf(
        R.string.sortLatest,
        R.string.sortPopular,
        R.string.sortPriceHighToLow,
        R.string.sortPriceLowToHigh
    )

    init {
        getProducts()
        selectedSortTitleLiveData.value = sortTitles[sort]
    }

    fun getProducts() {
        progressBarLiveData.value = true
        productRepository.getProductPopular(sort)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productLiveData.value = t
                }
            })
    }

    fun onSelectedSortChangeByUser(sort: Int) {
        this.sort = sort
        selectedSortTitleLiveData.value = sortTitles[sort]
        getProducts()

    }
    fun addProductToFavorite(product: Product) {
        if (product.isFavorite) {
            productRepository.deleteFromFavorite(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = false
                    }
                })
        } else {
            productRepository.addToFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = true
                    }
                })
        }
    }
}