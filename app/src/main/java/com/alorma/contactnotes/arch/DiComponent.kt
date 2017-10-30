package com.alorma.contactnotes.arch;

import com.alorma.contactnotes.ui.contacts.create.CreateContactActivity
import com.alorma.contactnotes.ui.overview.OverviewActivity
import dagger.Component

@Component
interface DiComponent {
    fun inject(activity: OverviewActivity)
    fun inject(activity: CreateContactActivity)
}