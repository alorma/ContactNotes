package com.alorma.contactnotes.ui.overview

import android.support.v4.app.FragmentActivity
import com.alorma.contactnotes.ui.Injector

object OverviewInjector : Injector() {
    fun provideOverviewViewModel(activity: FragmentActivity): OverviewViewModel {
        return provideViewModel(activity, OverviewViewModel::class.java)
    }
}