package com.alorma.contactnotes.data.notes

import com.alorma.contactnotes.domain.notes.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.internal.subscriptions.DeferredScalarSubscription
import org.reactivestreams.Subscriber
import java.util.*

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
            buildCollection(currentUser, userId).get().addOnCompleteListener { task ->
                parseTask(task, subscriber)
                subscriber.onComplete()
            }
        } else {
            subscriber.onError(Exception("Not logged"))
        }
    }.toList().toFlowable()

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
        val text = noteDocument.data[NOTE_DOCUMENT_ROW_TEXT] as String
        val date = noteDocument.data[NOTE_DOCUMENT_ROW_DATE]?.let {
            it as Date
        }
        return Note(id, text, date)
    }

    private fun buildCollection(currentUser: FirebaseUser, userId: String) =
            db.document("users/${currentUser.uid}")
                    .collection(NOTES_COLLECTION)
                    .whereEqualTo(NOTE_DOCUMENT_ROW_USER_ID, userId)
}