package com.example.nikestore.data.repo.order

import com.example.nikestore.data.Checkout
import com.example.nikestore.data.SubmitOrderResult
import io.reactivex.Single

interface OrderRepository {
    fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethode: String
    ):Single<SubmitOrderResult>

    fun checkOut(orderId:Int):Single<Checkout>
}