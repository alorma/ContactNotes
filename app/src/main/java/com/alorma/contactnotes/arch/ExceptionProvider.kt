package com.alorma.contactnotes.arch

import android.util.Log
import com.alorma.contactnotes.BuildConfig
import com.crashlytics.android.Crashlytics

class ExceptionProvider {
    fun onError(t: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e("ContactNotes:", "Error: ", t)
        } else {
            Crashlytics.getInstance().core.logException(t)
        }
    }
}