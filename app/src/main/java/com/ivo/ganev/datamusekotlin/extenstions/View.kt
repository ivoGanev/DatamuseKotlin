package com.ivo.ganev.datamusekotlin.extenstions

import android.view.View
import android.widget.EditText

infix fun View?.isWithId(id: Int) : Boolean {
    return this?.id == id
}

fun EditText.string() : String {
    return this.text.toString()
}