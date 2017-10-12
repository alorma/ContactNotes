package com.alorma.contactnotes.domain.validator

class MaxLengthRule(private val length: Int) : LengthRule(length) {
    override fun validate(textLength: Int, length: Int): Boolean = textLength < length

    override fun reason(t: String): String = "$t is ${t.length} length, and max characters allowed is $length"
}