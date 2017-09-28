package com.alorma.contactnotes.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

open class EitherLiveData<T> {

    private val valueLiveData = MutableLiveData<T>()
    private val errorLiveData = MutableLiveData<Throwable>()

    protected fun post(value: T) {
        valueLiveData.postValue(value)
    }

    protected fun post(throwable: Throwable) {
        errorLiveData.postValue(throwable)
    }

    fun subscribe(owner: LifecycleOwner, value: ((T) -> Unit)? = null, error: ((Throwable) -> Unit)? = null) {
        valueLiveData.observe(owner, Observer {
            it?.let {
                value?.invoke(it)
            }
        })
        errorLiveData.observe(owner, Observer {
            it?.let {
                error?.invoke(it)
            }
        })
    }
}