package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.data.contacts.persistance.ContactDBModel
import com.alorma.contactnotes.data.contacts.persistance.ContactsDAO
import com.alorma.contactnotes.domain.contacts.Contact
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

class RoomContactsDataSource(private val contactsDAO: ContactsDAO) : ContactsDataSource {

    override fun getContacts(): Flowable<List<Contact>> {
        return contactsDAO.getContacts()
                .map {
                    mapModels(it)
                }
    }

    private fun mapModels(dbModels: List<ContactDBModel>): List<Contact> = dbModels.map { mapModel(it) }

    private fun mapModel(dbModel: ContactDBModel) = Contact(dbModel.rawID, dbModel.name, dbModel.photo)

    override fun getContactByRawId(rawId: String): Maybe<Contact> = contactsDAO.getContactById(rawId).map { mapModel(it) }

    override fun insertContact(it: Contact): Completable {
        return Completable.fromAction({
            contactsDAO.insertContact(mapToDBModel(it))
        })
    }

    private fun mapToDBModel(contact: Contact) = ContactDBModel(contact.rawId, contact.name, contact.photo)
}
