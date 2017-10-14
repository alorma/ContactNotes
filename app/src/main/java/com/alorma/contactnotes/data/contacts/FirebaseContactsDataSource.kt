package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.internal.subscriptions.DeferredScalarSubscription
import org.reactivestreams.Subscriber
import java.util.*

class FirebaseContactsDataSource(auth: FirebaseAuth, private val db: FirebaseFirestore) : ContactsDataSource {
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
            buildCollection().get().addOnCompleteListener { task ->
                parseTask(task, subscriber)
                subscriber.onComplete()
            }.addOnFailureListener {
                subscriber.onError(it)
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

    override fun insertContact(createUserForm: CreateUserForm): Completable {
        return Completable.fromPublisher<Nothing> { subscriber ->

            if (currentUser != null) {
                val document = buildCollection().document()
                val task = document
                        .set(buildMapForFirebase(createUserForm), SetOptions.merge())
                runDocumentTask(task, subscriber)
            } else {
                subscriber.onError(Exception("Not logged"))
            }
        }
    }

    private fun runDocumentTask(task: Task<Void>, subscriber: Subscriber<in Nothing>) {
        task.addOnSuccessListener {
            subscriber.onComplete()
        }.addOnFailureListener {
            subscriber.onError(it)
        }
    }

    override fun update(id: String, createUserForm: CreateUserForm): Completable {
        return Completable.fromPublisher<Nothing> { subscriber ->

            if (currentUser != null) {
                val document = buildCollection().document(id)
                val task = document.update(buildMapForFirebase(createUserForm))
                runDocumentTask(task, subscriber)
            } else {
                subscriber.onError(Exception("Not logged"))
            }
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
                buildContactDocument(normalizeLookup(lookup)).get().addOnCompleteListener { task ->
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

    private fun buildCollection() =
            db.document("users/${currentUser?.uid}").collection(CONTACTS_COLLECTION)

    private fun buildContactDocument(lookup: String) =
            db.document("users/${currentUser?.uid}").collection(CONTACTS_COLLECTION)
                    .whereEqualTo(CONTACT_DOCUMENT_ROW_LOOKUP, lookup)

    override fun loadPhotoByLookup(lookup: String): Single<String> {
        return Single.never()
    }
}