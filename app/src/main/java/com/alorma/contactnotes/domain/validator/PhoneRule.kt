package com.alorma.contactnotes.domain.validator

class PhoneRule : ValidationRule<String, String> {
    override fun validate(t: String?): Boolean =
            android.util.Patterns.PHONE.matcher(t).matches()

    override fun reason(t: String): String = "$t is not a valid PHONE"
}