/**
 * Copyright (C) 2020 Ivo Ganev Open Source Project
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
package com.ivo.ganev.datamusekotlin.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.ivo.ganev.datamusekotlin.feature.api.ConstraintElement
import com.ivo.ganev.datamusekotlin.R

class HardConstraintAdapter(
    context: Context,
    private val hardConstraint: List<ConstraintElement>
) :
    BaseAdapter(), SpinnerAdapter {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return hardConstraint.size
    }

    override fun getItem(position: Int): Any {
        return hardConstraint[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.spinner_item,parent,false)
        val text = view.findViewById<TextView>(R.id.tv_spinner_item)
        text?.text = hardConstraint[position].label
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}
