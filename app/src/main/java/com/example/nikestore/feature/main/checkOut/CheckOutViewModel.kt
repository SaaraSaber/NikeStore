package com.example.nikestore.feature.main.checkOut

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.NikeViewModel
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.Checkout
import com.example.nikestore.data.repo.order.OrderRepository

class CheckOutViewModel(orderID: Int, orderRepository: OrderRepository) : NikeViewModel() {
    val checkoutLiveData = MutableLiveData<Checkout>()

    init {
        orderRepository.checkOut(orderID)
            .asyncNetworkRequest()
            .subscribe(object : NikeSingleObserver<Checkout>(compositeDisposable) {
                override fun onSuccess(t: Checkout) {
                    checkoutLiveData.value = t
                }

            })
    }
}