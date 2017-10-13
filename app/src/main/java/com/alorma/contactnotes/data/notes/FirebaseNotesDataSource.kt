package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.internal.subscriptions.DeferredScalarSubscription
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.util.*
import kotlin.collections.HashMap

class FirebaseNotesDataSource(auth: FirebaseAuth, private val db: FirebaseFirestore) : NotesDataSource {

    companion object {
        val NOTES_COLLECTION = "notes"

        val NOTE_DOCUMENT_ROW_USER_ID = "USER_ID"
        val NOTE_DOCUMENT_ROW_TEXT = "TEXT"
        val NOTE_DOCUMENT_ROW_DATE = "DATE"
    }

    private val currentUser = auth.currentUser

    override fun insertNote(userId: String, note: Note): Completable {
        return Completable.never()
    }

    override fun getNotesByUser(userId: String): Flowable<List<Note>> = Flowable.fromPublisher<Note> { subscriber ->
        val deferred: DeferredScalarSubscription<Note> = DeferredScalarSubscription(subscriber)
        subscriber.onSubscribe(deferred)
        if (currentUser != null) {
            buildCollection()
                    .whereEqualTo(NOTE_DOCUMENT_ROW_USER_ID, userId)
                    .get().addOnCompleteListener { task ->
                parseTask(task, subscriber)
                subscriber.onComplete()
            }
        } else {
            subscriber.onError(Exception("Not logged"))
        }
    }.toList().map {
        it.sortedByDescending { it.date }
    }.toFlowable()


    override fun getNote(noteId: String): Single<Note> = Single.fromPublisher<Note> { subscriber ->
        val deferred: DeferredScalarSubscription<Note> = DeferredScalarSubscription(subscriber)
        subscriber.onSubscribe(deferred)
        if (currentUser != null) {
            buildCollection().document(noteId)
                    .get().addOnCompleteListener { task ->
                parseDocumentTask(task, subscriber)
                subscriber.onComplete()
            }
        } else {
            subscriber.onError(Exception("Not logged"))
        }
    }

    private fun parseDocumentTask(task: Task<DocumentSnapshot>, subscriber: Subscriber<in Note>) {
        if (task.isSuccessful) {
            subscriber.onNext(parseItem(task.result))
            subscriber.onComplete()
        }
    }

    private fun parseTask(task: Task<QuerySnapshot>, subscriber: Subscriber<in Note>) {
        if (task.isSuccessful) {
            if (task.result != null) {
                parseTaskResult(task, subscriber)
            }
        } else {
            subscriber.onError(Exception(task.exception))
        }
    }

    private fun parseTaskResult(task: Task<QuerySnapshot>, subscriber: Subscriber<in Note>) {
        task.result.forEach {
            subscriber.onNext(parseItem(it))
        }
    }

    private fun parseItem(noteDocument: DocumentSnapshot): Note {
        val id = noteDocument.id
        val text = noteDocument.data[NOTE_DOCUMENT_ROW_TEXT]?.let { it as String } ?: ""
        val date = noteDocument.data[NOTE_DOCUMENT_ROW_DATE] as Long
        return Note(id, text, Date(date))
    }

    override fun createNote(contactId: String): Single<String> {
        return Single.fromPublisher({ subscriber ->
            val deferred: DeferredScalarSubscription<String> = DeferredScalarSubscription(subscriber)
            subscriber.onSubscribe(deferred)

            val map = HashMap<String, Any>().apply {
                put(NOTE_DOCUMENT_ROW_USER_ID, contactId)
                put(NOTE_DOCUMENT_ROW_DATE, System.currentTimeMillis())
            }

            val document = buildCollection().document()
            document.set(map, SetOptions.merge())
                    .addOnCompleteListener {
                        subscriber.onNext(document.id)
                        subscriber.onComplete()
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }

        })
    }

    override fun createNote(contactId: String, text: String): Single<String> {
        return Single.fromPublisher({ subscriber ->
            val deferred: DeferredScalarSubscription<String> = DeferredScalarSubscription(subscriber)
            subscriber.onSubscribe(deferred)

            val map = HashMap<String, Any>().apply {
                put(NOTE_DOCUMENT_ROW_USER_ID, contactId)
                put(NOTE_DOCUMENT_ROW_TEXT, text)
                put(NOTE_DOCUMENT_ROW_DATE, System.currentTimeMillis())
            }

            val document = buildCollection().document()
            document.set(map, SetOptions.merge())
                    .addOnCompleteListener {
                        subscriber.onNext(document.id)
                        subscriber.onComplete()
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }

        })
    }

    override fun updateNote(contactId: String, noteId: String, text: String): Single<String> {
        return Single.fromPublisher({ subscriber ->
            val deferred: DeferredScalarSubscription<String> = DeferredScalarSubscription(subscriber)
            subscriber.onSubscribe(deferred)

            val map = HashMap<String, Any>().apply {
                put(NOTE_DOCUMENT_ROW_TEXT, text)
                put(NOTE_DOCUMENT_ROW_DATE, System.currentTimeMillis())
            }

            val document = buildCollection().document(noteId)
            document.update(map)
                    .addOnCompleteListener {
                        subscriber.onNext(document.id)
                        subscriber.onComplete()
                    }
                    .addOnFailureListener {
                        subscriber.onError(it)
                    }

        })
    }

    private fun buildCollection() =
            db.document("users/${currentUser?.uid}")
                    .collection(NOTES_COLLECTION)
}