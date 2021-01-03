package com.ivo.ganev.datamusekotlin.extenstions

import android.view.View
import android.widget.EditText

/**
 * Checks if a given id is matched with the view's id
 * */
infix fun View?.isWithId(id: Int) : Boolean {
    return this?.id == id
}

/**
 * Provides the text value as a String
 * */
fun EditText.string() : String {
    return this.text.toString()
}