package com.alorma.contactnotes.ui.notes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.notes.Note
import kotlinx.android.synthetic.main.note_row.view.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val items = mutableListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.note_row, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.populate(items[position])

    override fun getItemCount() = items.size

    fun addAll(newItems: Collection<Note>) {
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateList(newItems: Collection<Note>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun populate(note: Note) {
            itemView.note.text = note.text
        }
    }
}