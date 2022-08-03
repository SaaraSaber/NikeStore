package com.example.nikestore.data.repo.source

import com.example.nikestore.data.Banner
import com.example.nikestore.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(val apiService: ApiService):BannerDataSource {
    override fun getBanners(): Single<List<Banner>> = apiService.getBanner()
}