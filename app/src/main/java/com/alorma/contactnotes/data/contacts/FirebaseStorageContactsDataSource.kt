package com.alorma.contactnotes.data.contacts

import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.domain.create.CreateUserForm
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.internal.subscriptions.DeferredScalarSubscription
import org.reactivestreams.Subscriber
import java.util.*

class FirebaseStorageContactsDataSource(auth: FirebaseAuth, private val db: FirebaseFirestore) : ContactsDataSource {

    companion object {
        val CONTACTS_COLLECTION = "contacts"
        val CONTACT_DOCUMENT_ROW_NAME = "NAME"
        val CONTACT_DOCUMENT_ROW_EMAIL = "EMAIL"
        val CONTACT_DOCUMENT_ROW_PHONE = "PHONE"
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
        return Contact(id, name)
    }

    override fun getContactByRawId(rawId: String): Maybe<Contact> {
        return Maybe.empty()
    }

    override fun insertContact(createUserForm: CreateUserForm): Completable {
        return Completable.fromPublisher<Nothing> { subscriber ->

            if (currentUser != null) {
                val map = HashMap<String, Any>().apply {
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
                }

                buildCollection(currentUser).document(UUID.randomUUID().toString())
                        .set(map, SetOptions.merge())
                        .addOnSuccessListener {
                            subscriber.onComplete()
                        }
                        .addOnFailureListener {
                            subscriber.onError(it)
                        }
            } else {
                subscriber.onError(Exception("Not logged"))
            }
        }
    }

    private fun buildCollection(currentUser: FirebaseUser) =
            db.document("users/${currentUser.uid}").collection(CONTACTS_COLLECTION)
}