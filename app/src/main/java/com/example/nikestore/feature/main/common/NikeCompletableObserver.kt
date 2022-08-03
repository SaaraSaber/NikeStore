package com.example.nikestore.feature.main.common

import com.example.nikestore.common.NikeExceptionMapper
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

abstract class NikeCompletableObserver(val compositeDisposable: CompositeDisposable) :
    CompletableObserver {

    override fun onError(e: Throwable) {
        EventBus.getDefault().post(NikeExceptionMapper.map(e))
        Timber.e(e)
    }


    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }
}
