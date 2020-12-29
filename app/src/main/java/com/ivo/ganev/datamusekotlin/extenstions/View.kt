package com.ivo.ganev.datamusekotlin.extenstions

import android.view.View

infix fun View?.isWithId(id: Int) : Boolean {
    return this?.id == id
}