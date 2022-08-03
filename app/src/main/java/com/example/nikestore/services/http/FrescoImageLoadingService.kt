package com.example.nikestore.services.http

import com.example.nikestore.view.NikeImageView
import com.facebook.drawee.view.SimpleDraweeView

class FrescoImageLoadingService:ImageLoadingService {
    override fun load(imageView: NikeImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(imageUrl)
        else
            throw IllegalStateException("imageView must be instance of simpleDraweeView")
    }
}