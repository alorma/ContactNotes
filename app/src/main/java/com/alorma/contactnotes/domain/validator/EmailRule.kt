package com.alorma.contactnotes.domain.validator

class EmailRule : ValidationRule<String, String> {
    override fun validate(t: String?): Boolean =
            android.util.Patterns.EMAIL_ADDRESS.matcher(t).matches()

    override fun reason(t: String): String = "$t is not a valid EMAIL"
}