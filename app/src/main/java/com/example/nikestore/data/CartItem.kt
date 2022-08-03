package com.example.nikestore.data

import android.service.voice.VoiceInteractionSession

data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: Product,
    var changeCountProgressBarIsVisible: Boolean = false
)