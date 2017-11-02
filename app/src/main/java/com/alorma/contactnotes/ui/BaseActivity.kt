package com.alorma.contactnotes.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alorma.contactnotes.arch.LifeCycleRelay

open class BaseActivity : AppCompatActivity() {

    val lifecycleRelay = LifeCycleRelay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(lifecycleRelay)
    }
}