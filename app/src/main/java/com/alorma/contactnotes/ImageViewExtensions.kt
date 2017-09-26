package com.alorma.contactnotes

import android.net.Uri
import android.widget.ImageView

fun ImageView.setImageURI(photo: String) {
    setImageURI(Uri.parse(photo))
}
