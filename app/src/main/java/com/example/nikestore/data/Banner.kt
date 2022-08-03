package com.example.nikestore.data

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Banner(
    val id: Int,
    val image: String,
    val link_type: Int,
    val link_value: String
): Parcelable