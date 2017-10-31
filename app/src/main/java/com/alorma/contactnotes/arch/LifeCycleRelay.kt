package com.alorma.contactnotes.arch

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.jakewharton.rxrelay2.BehaviorRelay

class LifeCycleRelay : DefaultLifecycleObserver {

    val lifecycle: BehaviorRelay<Lifecycle.Event> = BehaviorRelay.create()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        lifecycle.accept(Lifecycle.Event.ON_CREATE)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        lifecycle.accept(Lifecycle.Event.ON_START)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        lifecycle.accept(Lifecycle.Event.ON_RESUME)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        lifecycle.accept(Lifecycle.Event.ON_PAUSE)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        lifecycle.accept(Lifecycle.Event.ON_STOP)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        lifecycle.accept(Lifecycle.Event.ON_DESTROY)
        owner.lifecycle.removeObserver(this)
    }
}

