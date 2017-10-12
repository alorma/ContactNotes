package com.alorma.contactnotes.domain.validator

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class Validator<in T, R>(private vararg val rules: ValidationRule<T, R>) {

    private val reason = MutableLiveData<R>()

    fun getReason(): LiveData<R> = reason

    fun validate(t: T): Boolean {
        for (rule in rules) {
            if (!rule.validate(t)) {
                reason.postValue(rule.reason(t))
                return false
            }
        }
        return true
    }
}