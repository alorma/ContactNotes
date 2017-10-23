package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact

interface GetContact {
    fun get(userId: String): Contact?
}