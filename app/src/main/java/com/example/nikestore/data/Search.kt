package com.example.nikestore.data

data class Search(
    val category_id: Int,
    val discount: Int,
    val id: Int,
    val image: String,
    val price: Int,
    val status: Int,
    val title: String,
    val views: Int
)