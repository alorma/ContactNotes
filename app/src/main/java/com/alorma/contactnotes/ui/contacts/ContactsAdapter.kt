package com.alorma.contactnotes.ui.contacts

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

class ContactsAdapter(private val callback: (Contact) -> Unit) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private val items = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_row, parent, false), callback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.populate(items[position])

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View, private val callback: (Contact) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val contactImage: ImageView = itemView.findViewById(R.id.contactPhoto)
        private val contactName: TextView = itemView.findViewById(R.id.contactName)

        fun populate(contact: Contact) {
            contactName.text = contact.name

            val char = contact.name[0]
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder().buildRect(char.toString(), generator.getColor(char))
            contactImage.setImageDrawable(drawable)

            contact.photo?.let { contactImage.setImageURI(it) }
            
            itemView.setOnClickListener {
                callback.invoke(contact)
            }
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