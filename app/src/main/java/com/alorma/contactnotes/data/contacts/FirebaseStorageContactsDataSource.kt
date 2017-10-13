package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.internal.subscriptions.DeferredScalarSubscription
import org.reactivestreams.Subscriber
import java.util.*

class FirebaseStorageContactsDataSource(auth: FirebaseAuth, private val db: FirebaseFirestore) : ContactsDataSource {
    companion object {
        val CONTACTS_COLLECTION = "contacts"
        val CONTACT_DOCUMENT_ROW_NAME = "NAME"
        val CONTACT_DOCUMENT_ROW_EMAIL = "EMAIL"
        val CONTACT_DOCUMENT_ROW_PHONE = "PHONE"
        val CONTACT_DOCUMENT_ROW_LOOKUP = "LOOKUP"
    }

    private val currentUser = auth.currentUser

    override fun getContacts(): Flowable<List<Contact>> = Flowable.fromPublisher<Contact> { subscriber ->
        val deferred: DeferredScalarSubscription<Contact> = DeferredScalarSubscription(subscriber)
        subscriber.onSubscribe(deferred)
        if (currentUser != null) {
            buildCollection(currentUser).get().addOnCompleteListener { task ->
                parseTask(task, subscriber)
                subscriber.onComplete()
            }
        } else {
            subscriber.onError(Exception("Not logged"))
        }
    }.toList().toFlowable()

    private fun parseTask(task: Task<QuerySnapshot>, subscriber: Subscriber<in Contact>) {
        if (task.isSuccessful) {
            if (task.result != null) {
                parseTaskResult(task, subscriber)
            }
        } else {
            subscriber.onError(Exception(task.exception))
        }
    }

    private fun parseTaskResult(task: Task<QuerySnapshot>, subscriber: Subscriber<in Contact>) {
        task.result.forEach {
            subscriber.onNext(parseItem(it))
        }
    }

    private fun parseItem(contactDocument: DocumentSnapshot): Contact {
        val id = contactDocument.id
        val name = contactDocument.data[CONTACT_DOCUMENT_ROW_NAME] as String
        val email = contactDocument.data[CONTACT_DOCUMENT_ROW_EMAIL]?.let { it as String }
        val phone = contactDocument.data[CONTACT_DOCUMENT_ROW_PHONE]?.let { it as String }
        val lookup = contactDocument.data[CONTACT_DOCUMENT_ROW_LOOKUP]?.let { it as String }
        return Contact(id, name, userEmail = email, userPhone = phone, lookup = lookup)
    }

    override fun getContactByRawId(rawId: String): Maybe<Contact> {
        return Maybe.empty()
    }

    override fun insertContact(createUserForm: CreateUserForm): Completable {
        return Completable.fromPublisher<Nothing> { subscriber ->

            if (currentUser != null) {
                val documentReference = buildCollection(currentUser).document(UUID.randomUUID().toString())
                workWithDocumentRef(documentReference, createUserForm, subscriber)
            } else {
                subscriber.onError(Exception("Not logged"))
            }
        }
    }

    override fun update(id: String, createUserForm: CreateUserForm): Completable {
        return Completable.fromPublisher<Nothing> { subscriber ->

            if (currentUser != null) {
                val documentReference = buildCollection(currentUser).document(id)
                workWithDocumentRef(documentReference, createUserForm, subscriber)
            } else {
                subscriber.onError(Exception("Not logged"))
            }
        }
    }

    private fun workWithDocumentRef(documentReference: DocumentReference, createUserForm: CreateUserForm, subscriber: Subscriber<in Nothing>) {
        documentReference
                .update(buildMapForFirebase(createUserForm))
                .addOnSuccessListener {
                    subscriber.onComplete()
                }
                .addOnFailureListener {
                    subscriber.onError(it)
                }
    }

    private fun buildMapForFirebase(createUserForm: CreateUserForm): HashMap<String, Any> = HashMap<String, Any>().apply {
        put(CONTACT_DOCUMENT_ROW_NAME, createUserForm.userName)
        createUserForm.userEmail?.let {
            if (it.isNotEmpty()) {
                put(CONTACT_DOCUMENT_ROW_EMAIL, it)
            }
        }
        createUserForm.userPhone?.let {
            if (it.isNotEmpty()) {
                put(CONTACT_DOCUMENT_ROW_PHONE, it)
            }
        }
        createUserForm.lookup?.let {
            if (it.isNotEmpty()) {
                put(CONTACT_DOCUMENT_ROW_LOOKUP, normalizeLookup(it))
            }
        }
    }

    override fun loadContactByLookup(lookup: String): Single<Contact> {
        return Single.fromPublisher({ subscriber ->
            val deferred: DeferredScalarSubscription<Contact> = DeferredScalarSubscription(subscriber)
            subscriber.onSubscribe(deferred)

            if (currentUser != null) {
                buildContactDocument(currentUser, normalizeLookup(lookup)).get().addOnCompleteListener { task ->
                    parseTask(task, subscriber)
                    subscriber.onComplete()
                }
            } else {
                subscriber.onError(Exception("Not logged"))
            }
        })
    }

    private fun normalizeLookup(lookup: String) = lookup.split(".")[0]

    override fun getLookupKey(contactUri: String): Single<String> {
        return Single.never()
    }

    private fun buildCollection(currentUser: FirebaseUser) =
            db.document("users/${currentUser.uid}").collection(CONTACTS_COLLECTION)

    private fun buildContactDocument(currentUser: FirebaseUser, lookup: String) =
            db.document("users/${currentUser.uid}").collection(CONTACTS_COLLECTION)
                    .whereEqualTo(CONTACT_DOCUMENT_ROW_LOOKUP, lookup)
}