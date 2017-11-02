package com.alorma.contactnotes.arch

import android.util.Log

class LogExceptionProvider : ExceptionProvider {
    override fun onError(t: Throwable) {
        Log.e("ContactNotes:", "Error: ", t)
    }
}