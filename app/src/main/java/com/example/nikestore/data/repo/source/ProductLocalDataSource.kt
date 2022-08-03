package com.example.nikestore.data.repo.source

import androidx.room.*
import com.example.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductLocalDataSource : ProductDataSource {

    override fun getProduct(sort: Int): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun search(q: String): Single<ArrayList<Product>> {
        TODO("Not yet implemented")
    }

    @Query("SELECT * FROM products")
    override fun getFavoriteProduct(): Single<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorites(product: Product): Completable

    @Delete
    override fun deleteFromFavorite(product: Product): Completable
}