package com.alorma.contactnotes.domain.validator

class NotEmptyRule : ValidationRule<String, String> {

    override fun validate(t: String?): Boolean = t != null && t.trim().isNotEmpty()

    override fun reason(t: String): String = "Text cannot be empty"
}