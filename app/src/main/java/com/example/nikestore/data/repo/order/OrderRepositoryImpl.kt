package com.example.nikestore.data.repo.order

import com.example.nikestore.data.Checkout
import com.example.nikestore.data.SubmitOrderResult
import io.reactivex.Single

class OrderRepositoryImpl(private val orderDataSource: OrderDataSource) : OrderRepository {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethode: String
    ): Single<SubmitOrderResult> {
        return orderDataSource.submit(
            firstName,
            lastName,
            postalCode,
            phoneNumber,
            address,
            paymentMethode
        )
    }

    override fun checkOut(orderId: Int): Single<Checkout> {
        return orderDataSource.checkOut(orderId)
    }
}