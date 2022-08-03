package com.example.nikestore.feature.main.home

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.nikestore.NikeViewModel
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.data.*
import com.example.nikestore.data.repo.BannerRepository
import com.example.nikestore.data.repo.ProductRepository
import com.example.nikestore.feature.main.common.NikeCompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class HomeViewModel(
    private val productRepository: ProductRepository,
    bannerRepository: BannerRepository
) :
    NikeViewModel() {
    val productLiveDataLatest = MutableLiveData<List<Product>>()
    val productLiveDataPopular = MutableLiveData<List<Product>>()
    val bannerLiveData = MutableLiveData<List<Banner>>()
    val productLiveData = MutableLiveData<List<Product>>()

    init {
        progressBarLiveData.value = true
        productRepository.getProductsLatest(SORT_LATEST)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productLiveDataLatest.value = t
                }
            })

        productRepository.getProductPopular(SORT_POPULAR)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productLiveDataPopular.value = t
                }
            })

        bannerRepository.getBanners()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value = t
                }
            })
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

    fun search(searchText: String) {
        productRepository.search(searchText)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : NikeSingleObserver<ArrayList<Product>>(compositeDisposable) {
                override fun onSuccess(t: ArrayList<Product>) {
                    Timber.d("1")
                    if (t.isNotEmpty())
                        productLiveData.value = t
                    else {
                        productLiveData.value = emptyList()
                    }

                }
            })
    }
}

