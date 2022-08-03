package com.example.nikestore.data.repo

import com.example.nikestore.data.AddToCardResponse
import com.example.nikestore.data.CartItemCount
import com.example.nikestore.data.CartResponse
import com.example.nikestore.data.MessageResponse
import io.reactivex.Single

interface CartRepository {

    fun addToCart(productId: Int): Single<AddToCardResponse>
    fun get(): Single<CartResponse>
    fun remove(cartItemId: Int): Single<MessageResponse>
    fun changeCount(cartItemId: Int, count: Int): Single<AddToCardResponse>
    fun getCartItemCount(): Single<CartItemCount>
}