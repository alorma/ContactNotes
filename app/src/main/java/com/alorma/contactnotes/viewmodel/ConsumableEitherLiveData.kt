package com.alorma.contactnotes.viewmodel

import android.arch.lifecycle.MutableLiveData
import io.reactivex.MaybeObserver
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class ConsumableEitherLiveData<T> : EitherLiveData<T>(), Subscriber<T>, MaybeObserver<T> {

    val disposableLiveData = MutableLiveData<Disposable>()
    val subscribedLiveData = MutableLiveData<Subscription>()
    val completeLiveData = MutableLiveData<Unit>()

    override fun onSubscribe(d: Disposable) {
        disposableLiveData.postValue(d)
    }

    override fun onSuccess(t: T) {
        post(t)
    }

    override fun onNext(t: T) {
        post(t)
    }

    override fun onError(t: Throwable) {
        post(t)
    }

    override fun onComplete() {
        completeLiveData.postValue(Unit)
    }

    override fun onSubscribe(s: Subscription) {
        s.request(Long.MAX_VALUE)
        subscribedLiveData.postValue(s)
    }

}