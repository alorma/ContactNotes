package com.alorma.contactnotes.ui.notes

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.notes.Note
import kotlinx.android.synthetic.main.note_row.view.*

class NotesAdapter(private val callback: (Note) -> Unit, private val callbackLong: (Note) -> Boolean) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private val items = mutableListOf<Note>()
    private val selectedItems = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.note_row, parent, false), callback, callbackLong, selectedItems)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.populate(items[position])

    override fun getItemCount() = items.size

    fun updateList(newItems: Collection<Note>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View,
                     private val callback: (Note) -> Unit,
                     private val callbackLong: (Note) -> Boolean,
                     private val selectedItems: MutableSet<String>) : RecyclerView.ViewHolder(itemView) {
        fun populate(note: Note) {
            itemView.note.text = note.text

            itemView.noteCard.setCardBackgroundColor(ContextCompat.getColor(itemView.noteCard.context,
                    if (selectedItems.contains(note.id)) {
                        R.color.grey_300
                    } else {
                        android.R.color.white
                    }))

            itemView.setOnClickListener {
                if (selectedItems.isNotEmpty() || selectedItems.contains(note.id)) {
                    callbackLong.invoke(note)
                } else {
                    callback.invoke(note)
                }
            }

            itemView.setOnLongClickListener {
                callbackLong.invoke(note)
            }
        }
    }

    fun setSelectedItems(it: Set<String>) {
        this.selectedItems.clear()
        this.selectedItems.addAll(it)
        notifyDataSetChanged()
    }
}