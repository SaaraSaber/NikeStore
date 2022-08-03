package com.example.nikestore.data

data class Checkout(
    val payable_price: Int,
    val payment_status: String,
    val purchase_success: Boolean
)