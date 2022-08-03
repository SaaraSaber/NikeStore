package com.example.nikestore.services.http

import com.example.nikestore.view.NikeImageView

interface ImageLoadingService {
    fun load(imageView: NikeImageView, imageUrl:String)
}