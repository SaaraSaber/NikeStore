package com.example.nikestore.data.repo

import com.example.nikestore.data.Product
import com.example.nikestore.data.repo.source.ProductDataSource
import com.example.nikestore.data.repo.source.ProductLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    val remoteDataSource: ProductDataSource,
    val localDataSource: ProductLocalDataSource
) : ProductRepository {
    override fun getProductsLatest(sort: Int): Single<List<Product>> =
        localDataSource.getFavoriteProduct().flatMap { favoriteProduct ->
            remoteDataSource.getProduct(sort).doOnSuccess {
                val favoriteProductIds = favoriteProduct.map {
                    it.id
                }
                it.forEach { product ->
                    if (favoriteProductIds.contains(product.id))
                        product.isFavorite = true
                }
            }
        }

    override fun getProductPopular(sort: Int): Single<List<Product>> =
        remoteDataSource.getProduct(sort)

    override fun getFavoriteProduct(): Single<List<Product>> = localDataSource.getFavoriteProduct()

    override fun addToFavorites(product: Product): Completable =
        localDataSource.addToFavorites(product)

    override fun deleteFromFavorite(product: Product): Completable =
        localDataSource.deleteFromFavorite(product)

    override fun search(q: String): Single<ArrayList<Product>> =
        remoteDataSource.search(q)

}