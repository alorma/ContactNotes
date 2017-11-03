package com.alorma.contactnotes.domain.notes

import com.alorma.contactnotes.domain.validator.ValidationRule

class ValidNoteRule : ValidationRule<Note, Exception> {
    override fun validate(t: Note?): Boolean = t?.text.isNullOrBlank().not()

    override fun reason(t: Note): Exception = Exception()
}