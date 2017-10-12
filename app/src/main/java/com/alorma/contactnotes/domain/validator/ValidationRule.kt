package com.alorma.contactnotes.domain.validator

interface ValidationRule<in T, out R> {
    fun validate(t: T?): Boolean

    fun reason(t: T): R
}