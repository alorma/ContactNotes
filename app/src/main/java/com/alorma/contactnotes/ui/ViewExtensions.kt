package com.alorma.contactnotes.ui

import android.view.View

fun <T : View> T?.toggle(visibility: Int) {
    this?.let {
        if (this.visibility != visibility) {
            this.visibility = visibility
        } else {
            this.visibility = View.VISIBLE
        }
    }
}