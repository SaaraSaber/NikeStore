package com.example.nikestore.data.repo.source

import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductDataSource {
    fun getProduct(sort: Int): Single<List<Product>>

    fun getFavoriteProduct(): Single<List<Product>>

    fun addToFavorites(product: Product): Completable

    fun deleteFromFavorite(product: Product): Completable

    fun search(q: String): Single<ArrayList<Product>>
}
