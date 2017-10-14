package com.alorma.contactnotes.ui.overview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alorma.contactnotes.R
import com.alorma.contactnotes.domain.contacts.Contact
import com.alorma.contactnotes.setImageURI
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

class ContactsOverviewAdapter(private val callback: (Contact) -> Unit) : RecyclerView.Adapter<ContactsOverviewAdapter.ViewHolder>() {

    private val items = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_overview_row, parent, false), callback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.populate(items[position])

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View, private val callback: (Contact) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val contactImage: ImageView = itemView.findViewById(R.id.contactPhoto)
        private val contactName: TextView = itemView.findViewById(R.id.contactName)
        private val contactNote: TextView = itemView.findViewById(R.id.contactNote)
        private val noNotesText: TextView = itemView.findViewById(R.id.noNotesText)

        fun populate(contact: Contact) {
            contactName.text = contact.name

            val drawable = getContactDefaultDrawable(contact)

            val options = RequestOptions()
                    .centerCrop()
                    .error(drawable)
                    .placeholder(drawable)
                    .fallback(drawable)
                    .priority(Priority.HIGH)
                    .transforms(CircleCrop())

            Glide.with(contactImage)
                    .load(contact.photo)
                    .apply(options)
                    .into(contactImage)

            contact.photo?.let {
                if (it.isNotEmpty()) {
                    contactImage.setImageURI(it)
                }
            }

            contact.notes?.let {
                if (it.isNotEmpty()) {
                    val text = it[0].text
                    val maxLength = Math.min(text.length, 100) - 1
                    val substringRange = 0..maxLength
                    contactNote.text = text[substringRange]
                    contactNote.visibility = View.VISIBLE
                    noNotesText.visibility = View.GONE
                } else {
                    contactNote.visibility = View.GONE
                    noNotesText.visibility = View.VISIBLE
                }
            }

            itemView.setOnClickListener {
                callback.invoke(contact)
            }
        }

        private fun getContactDefaultDrawable(contact: Contact): TextDrawable {
            val char = contact.name[0]
            val generator = ColorGenerator.MATERIAL
            return TextDrawable.builder().buildRound(char.toString(), generator.getColor(char))
        }
    }

    fun addItems(contacts: Collection<Contact>) {
        items.addAll(contacts)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}

private operator fun String.get(intRange: IntRange): String = this.substring(intRange)
