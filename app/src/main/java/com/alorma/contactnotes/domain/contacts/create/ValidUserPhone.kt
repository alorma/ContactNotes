package com.alorma.contactnotes.domain.contacts.create

import com.alorma.contactnotes.domain.exception.CreateUserException
import com.alorma.contactnotes.domain.exception.UserPhoneException
import com.alorma.contactnotes.domain.validator.ValidationRule

class ValidUserPhone(private vararg val rules: ValidationRule<String, String>) : ValidationRule<CreateUserForm, CreateUserException> {
    override fun validate(t: CreateUserForm?): Boolean {
        return t?.userPhone?.let {
            it.isEmpty() || check(it)
        } ?: true
    }

    private lateinit var rule: ValidationRule<String, String>

    private fun check(phone: String): Boolean {
        return rules.any {
            val validate = it.validate(phone)

            this.rule = it

            !validate
        }.not()
    }

    override fun reason(t: CreateUserForm): CreateUserException {
        return UserPhoneException(rule.reason(t.userPhone ?: ""))
    }

}