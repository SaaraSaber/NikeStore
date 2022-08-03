package com.example.nikestore

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.example.nikestore.data.db.AppDataBase
import com.example.nikestore.data.repo.*
import com.example.nikestore.data.repo.order.OrderRemoteDataSource
import com.example.nikestore.data.repo.order.OrderRepository
import com.example.nikestore.data.repo.order.OrderRepositoryImpl
import com.example.nikestore.data.repo.source.*
import com.example.nikestore.feature.main.MainViewModel
import com.example.nikestore.feature.main.auth.AuthViewModel
import com.example.nikestore.feature.main.cart.CartViewModel
import com.example.nikestore.feature.main.checkOut.CheckOutViewModel
import com.example.nikestore.feature.main.home.HomeViewModel
import com.example.nikestore.feature.main.product.ProductDetailViewModel
import com.example.nikestore.feature.main.common.ProductListAdapter
import com.example.nikestore.feature.main.favorite.FavoriteViewModel
import com.example.nikestore.feature.main.list.ProductListViewModel
import com.example.nikestore.feature.main.product.comment.CommentListViewModel
import com.example.nikestore.feature.main.profile.ProfileViewModel
import com.example.nikestore.feature.main.shipping.ShippingViewModel
import com.example.nikestore.services.http.FrescoImageLoadingService
import com.example.nikestore.services.http.ImageLoadingService
import com.example.nikestore.services.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber


class App : Application() {

    override fun onCreate() {

        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)

        val myModules = module {
            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            single { Room.databaseBuilder(this@App,AppDataBase::class.java,"db_app").build() }
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    get<AppDataBase>().productDao()
                )
            }

            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings", MODE_PRIVATE
                )
            }
            single { UserLocalDataSource(get()) }
            single<UserRepository> {
                UserRepositoryImpl(
                    UserLocalDataSource(get()),
                    UserRemoteDataSource(get())
                )
            }
            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }

            factory { (viewType: Int) -> ProductListAdapter(viewType, get()) }
            factory<BannerRepository> {
                BannerRepositoryImpl(BannerRemoteDataSource(get()))
            }
            factory<CommentRepository> { CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }

            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(bundle, get(), get()) }
            viewModel { (productId: Int) -> CommentListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel { (orderId: Int) -> CheckOutViewModel(orderId, get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel { FavoriteViewModel(get()) }
        }
        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}