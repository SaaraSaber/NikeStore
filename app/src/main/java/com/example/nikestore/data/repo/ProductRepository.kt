package com.example.nikestore.data.repo

import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductRepository {
    fun getProductsLatest(sort: Int): Single<List<Product>>

    fun getProductPopular(sort: Int): Single<List<Product>>

    fun getFavoriteProduct(): Single<List<Product>>

    fun addToFavorites(product: Product): Completable

    fun deleteFromFavorite(product: Product): Completable

    fun search(q: String): Single<ArrayList<Product>>
}