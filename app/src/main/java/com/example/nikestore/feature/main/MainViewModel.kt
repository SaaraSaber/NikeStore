package com.example.nikestore.feature.main

import com.example.nikestore.NikeViewModel
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.data.TokenContainer
import com.example.nikestore.data.repo.CartRepository
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(private val cartRepository: CartRepository) : NikeViewModel() {
    fun getCartItemCount() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            cartRepository.getCartItemCount()
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeSingleObserver<CartItemCount>(compositeDisposable) {
                    override fun onSuccess(t: CartItemCount) {
                        EventBus.getDefault().postSticky(t)
                    }
                })
        }
    }
}