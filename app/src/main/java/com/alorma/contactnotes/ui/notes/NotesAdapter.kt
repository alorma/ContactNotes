package com.alorma.contactnotes.ui.notes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.notes.Note
import com.alorma.contactnotes.ui.toggle
import kotlinx.android.synthetic.main.note_row.view.*

class NotesAdapter(private val callback: (Note) -> Unit) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val items = mutableListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.note_row, parent, false), callback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.populate(items[position])

    override fun getItemCount() = items.size

    fun updateList(newItems: Collection<Note>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val callback: (Note) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun populate(note: Note) {
            itemView.note.text = note.text

            itemView.setOnClickListener {
                if (itemView.contextualActions.visibility != View.VISIBLE) {
                    callback.invoke(note)
                }
            }

            itemView.setOnLongClickListener {
                itemView.contextualActions.toggle(View.GONE)
                true
            }

            itemView.contextualActions.setOnClickListener {
                itemView.contextualActions.toggle(View.GONE)
            }

            itemView.contextualActions.deleteAction.setOnClickListener {

            }
        }
    }
}