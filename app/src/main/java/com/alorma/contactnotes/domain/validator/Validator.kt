package com.alorma.contactnotes.domain.validator

import com.alorma.contactnotes.arch.Either
import com.alorma.contactnotes.arch.Left
import com.alorma.contactnotes.arch.Right

class Validator<T, out R>(private vararg val rules: ValidationRule<T, R>) {
    fun validate(t: T): Either<R, T> {
        return rules
                .firstOrNull { !it.validate(t) }
                ?.let { Left(it.reason(t)) }
                ?: Right(t)
    }
}