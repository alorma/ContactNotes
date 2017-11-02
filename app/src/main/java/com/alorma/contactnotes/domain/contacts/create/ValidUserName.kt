package com.alorma.contactnotes.domain.contacts.create

import com.alorma.contactnotes.domain.exception.CreateUserException
import com.alorma.contactnotes.domain.exception.UserNameException
import com.alorma.contactnotes.domain.validator.ValidationRule

class ValidUserName(private vararg val rules: ValidationRule<String, String>) : ValidationRule<CreateUserForm, CreateUserException> {
    override fun validate(t: CreateUserForm?): Boolean {
        return t?.map { it.userName }?.let {
            check(it)
        } ?: false
    }

    private lateinit var rule: ValidationRule<String, String>

    private fun check(name: String): Boolean {
        return rules.any {
            val validate = it.validate(name)

            this.rule = it

            !validate
        }.not()
    }

    override fun reason(t: CreateUserForm): CreateUserException {
        return UserNameException(rule.reason(t.userName))
    }
}
