package com.example.nikestore.feature.main.favorite

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.NikeViewModel
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.ProductRepository
import com.example.nikestore.feature.main.common.NikeCompletableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FavoriteViewModel(val productRepository: ProductRepository) : NikeViewModel() {
    val productLiveData = MutableLiveData<List<Product>>()

    init {
        productRepository.getFavoriteProduct()
            .subscribeOn(Schedulers.io())
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productLiveData.postValue(t)
                }

            })
    }

    fun removeFromFavorite(product: Product) {
        productRepository.deleteFromFavorite(product)
            .subscribeOn(Schedulers.io())
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("removeFromFavorite complete")
                }
            })
    }
}