package com.alorma.contactnotes.arch

import android.arch.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<MutableList<T>>() {
    fun add(t: T) {
        val list = value ?: mutableListOf()
        list.add(t)
        value = list
    }

    fun addAll(items: List<T>) {
        val list = value ?: mutableListOf()
        list.addAll(items)
        value = list
    }

    fun clear() {
        value = mutableListOf()
    }

    fun count(): Int {
        return value?.size ?: 0
    }
}