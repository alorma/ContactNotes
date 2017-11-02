package com.alorma.contactnotes.arch

interface ExceptionProvider {
    fun onError(t: Throwable)
}