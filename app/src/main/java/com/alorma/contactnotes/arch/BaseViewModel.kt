package com.alorma.contactnotes.arch

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable

open class BaseViewModel : ViewModel() {
    fun filterState(lifecycleRelay: Observable<Lifecycle.Event>, state: Lifecycle.Event) = lifecycleRelay.filter { it == state }
}