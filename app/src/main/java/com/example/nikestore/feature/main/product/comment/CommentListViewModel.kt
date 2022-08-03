package com.example.nikestore.feature.main.product.comment

import androidx.lifecycle.MutableLiveData
import com.example.nikestore.NikeViewModel
import com.example.nikestore.common.NikeSingleObserver
import com.example.nikestore.common.asyncNetworkRequest
import com.example.nikestore.data.Comment
import com.example.nikestore.data.repo.CommentRepository

class CommentListViewModel(productId: Int, commentRepository: CommentRepository) : NikeViewModel() {
    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {
        progressBarLiveData.value = true
        commentRepository.getAll(productId)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    commentsLiveData.value = t
                }

            })
    }
}