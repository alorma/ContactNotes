package com.alorma.contactnotes.domain.contacts.create

import com.alorma.contactnotes.domain.validator.ValidationRule

class ValidUserPhone(private vararg val rules: ValidationRule<String, String>) : ValidationRule<CreateUserForm, Exception> {
    override fun validate(t: CreateUserForm?): Boolean {
        return t?.let {
            it.userPhone?.let { check(it) } ?: true
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

    override fun reason(t: CreateUserForm): Exception {
        return Exception()
    }

}