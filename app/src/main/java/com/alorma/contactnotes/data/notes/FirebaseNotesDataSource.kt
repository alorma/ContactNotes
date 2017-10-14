package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import io.reactivex.Flowable
import io.reactivex.internal.subscriptions.DeferredScalarSubscription
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

    override fun getNotesFromUser(userId: String): Flowable<List<Note>> = Flowable.fromPublisher<Note> { subscriber ->
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

    override fun getNotesByUser(userId: String) {
        buildCollection()
                .whereEqualTo(NOTE_DOCUMENT_ROW_USER_ID, userId)
                .get().addOnCompleteListener { task ->

            if (task.isSuccessful) {
                if (task.result != null) {
                    NotesListLiveData.INSTANCE.postValue(task.result.map { parseItem(it) })
                }
            } else {
                ErrorNotesLiveData.INSTANCE.postValue(task.exception)
            }
        }.addOnFailureListener {
            ErrorNotesLiveData.INSTANCE.postValue(it)
        }
    }

    override fun getNote(noteId: String) {
        if (currentUser != null) {
            buildCollection().document(noteId).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    SingleNoteLiveData.INSTANCE.postValue(parseItem(task.result))
                }
            }.addOnFailureListener {
                ErrorNotesLiveData.INSTANCE.postValue(it)
            }
        } else {
            ErrorNotesLiveData.INSTANCE.postValue(Exception("Not logged"))
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

    override fun createNote(contactId: String) {
        val map = HashMap<String, Any>().apply {
            put(NOTE_DOCUMENT_ROW_USER_ID, contactId)
            put(NOTE_DOCUMENT_ROW_DATE, System.currentTimeMillis())
        }

        val document = buildCollection().document()
        document.set(map, SetOptions.merge())
                .addOnCompleteListener {
                    InsertNoteLiveData.INSTANCE.postValue(document.id)
                }
                .addOnFailureListener {
                    ErrorNotesLiveData.INSTANCE.postValue(it)
                }
    }

    override fun createNote(contactId: String, text: String) {
        val map = HashMap<String, Any>().apply {
            put(NOTE_DOCUMENT_ROW_USER_ID, contactId)
            put(NOTE_DOCUMENT_ROW_TEXT, text)
            put(NOTE_DOCUMENT_ROW_DATE, System.currentTimeMillis())
        }

        val document = buildCollection().document()
        document.set(map, SetOptions.merge())
                .addOnCompleteListener {
                    InsertNoteLiveData.INSTANCE.postValue(document.id)
                }
                .addOnFailureListener {
                    ErrorNotesLiveData.INSTANCE.postValue(it)
                }
    }

    override fun updateNote(contactId: String, noteId: String, text: String) {
        val map = HashMap<String, Any>().apply {
            put(NOTE_DOCUMENT_ROW_TEXT, text)
            put(NOTE_DOCUMENT_ROW_DATE, System.currentTimeMillis())
        }

        val document = buildCollection().document(noteId)
        document.update(map)
                .addOnCompleteListener {
                    InsertNoteLiveData.INSTANCE.postValue(document.id)
                }
                .addOnFailureListener {
                    ErrorNotesLiveData.INSTANCE.postValue(it)
                }

    }

    private fun buildCollection() =
            db.document("users/${currentUser?.uid}")
                    .collection(NOTES_COLLECTION)
}