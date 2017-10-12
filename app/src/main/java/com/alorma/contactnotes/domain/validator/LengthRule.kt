package com.alorma.contactnotes.domain.validator

abstract class LengthRule(private val length: Int) : ValidationRule<String, String> {
    override fun validate(t: String?): Boolean {
        if (t == null) {
            return false
        }
        return validate(t.length, length)
    }

    abstract fun validate(textLength: Int, length: Int): Boolean
}