package com.example.nikestore.data.repo.source

import com.example.nikestore.data.Product
import com.example.nikestore.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(val apiService: ApiService) : ProductDataSource {

    override fun getProduct(sort: Int): Single<List<Product>> =
        apiService.getProducts(sort.toString())

    override fun getFavoriteProduct(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorite(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun search(q: String): Single<ArrayList<Product>> = apiService.search(q)

}