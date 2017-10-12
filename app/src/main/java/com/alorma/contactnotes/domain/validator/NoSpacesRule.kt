package com.alorma.contactnotes.domain.validator

class NoSpacesRule : ValidationRule<String, String> {
    override fun validate(t: String?): Boolean = t?.contains(" ")?.not() ?: false

    override fun reason(t: String): String = "$t contains spaces at ${t.indexOf(" ")} position"
}