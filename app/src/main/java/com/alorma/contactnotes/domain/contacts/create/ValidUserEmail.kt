package com.alorma.contactnotes.domain.contacts.create

import com.alorma.contactnotes.domain.exception.UserEmailException
import com.alorma.contactnotes.domain.validator.ValidationRule

class ValidUserEmail(private vararg val rules: ValidationRule<String, String>) : ValidationRule<CreateUserForm, Exception> {
    override fun validate(t: CreateUserForm?): Boolean {
        return t?.userEmail?.let {
            it.isEmpty() || check(it)
        } ?: true
    }

    private fun check(name: String): Boolean {
        return rules.any { !it.validate(name) }.not()
    }

    override fun reason(t: CreateUserForm): Exception {
        return UserEmailException()
    }

}