package com.example.nikestore.feature.main.product

import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.NikeActivity
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_Id

import com.example.nikestore.common.formatPrice
import com.example.nikestore.data.Comment
import com.example.nikestore.databinding.ActivityProductDetailBinding
import com.example.nikestore.feature.main.common.NikeCompletableObserver
import com.example.nikestore.feature.main.product.comment.CommentAdapter
import com.example.nikestore.feature.main.product.comment.CommentListActivity
import com.example.nikestore.services.http.ImageLoadingService
import com.example.nikestore.view.scroll.ObservableScrollViewCallbacks
import com.example.nikestore.view.scroll.ScrollState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : NikeActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    val productDetailViewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()
    val adapterComment = CommentAdapter()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productDetailViewModel.productLiveData.observe(this) {
            imageLoadingService.load(binding.productIv, it.image)
            binding.titleTv.text = it.title
            binding.previousPriceTv.text = formatPrice(it.previous_price)
            binding.previousPriceTv.paintFlags = STRIKE_THRU_TEXT_FLAG
            binding.currentPriceTv.text = formatPrice(it.price)
            binding.toolbarTitleTv.text = it.title
        }

        productDetailViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        productDetailViewModel.commentLiveDta.observe(this) {
            Timber.i(it.toString())
            adapterComment.comments = it as ArrayList<Comment>
            if (it.size > 3) {
                binding.viewAllCommentsBtn.visibility = View.VISIBLE
                binding.viewAllCommentsBtn.setOnClickListener {
                    startActivity(Intent(this, CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_Id, productDetailViewModel.productLiveData.value!!.id)
                    })
                }
            }
        }
        initViews()
    }

    fun initViews() {
        binding.commentRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.commentRv.adapter = adapterComment
        binding.commentRv.isNestedScrollingEnabled = false

        binding.productIv.post {
            val toolbar = binding.toolbarView
            val productIvHeight = binding.productIv.height
            val productImageView = binding.productIv

            binding.observableScrollView.addScrollViewCallbacks(object :
                ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {
                    toolbar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }
            })
        }

        binding.addToCartBtn.setOnClickListener {
            productDetailViewModel.onAddToCartBtn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        showSnackBar(getString(R.string.success_addToCart))
                    }
                })
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}