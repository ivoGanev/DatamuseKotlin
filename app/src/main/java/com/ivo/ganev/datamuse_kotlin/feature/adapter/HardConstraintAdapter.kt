/**
 * Copyright (C) 2020 Ivo Ganev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivo.ganev.datamuse_kotlin.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.ivo.ganev.datamuse_kotlin.R
import com.ivo.ganev.datamuse_kotlin.feature.ConstraintElement.*

class HardConstraintAdapter(
    context: Context,
) : BaseAdapter(), SpinnerAdapter {

    /*
     * The list is related only with this adapter and its read-only
     * so there is no point to stay in the activity.
     * */
    val constraints = listOf(
        MeansLikeElement, SoundsLikeElement, SpelledLikeElement, RelatedWordsElement
    )

    private val layoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return constraints.size
    }

    override fun getItem(position: Int): Any {
        return constraints[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.spinner_item, parent, false)
        val text = view.findViewById<TextView>(R.id.tv_spinner_item)
        text?.text = constraints[position].label
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}
