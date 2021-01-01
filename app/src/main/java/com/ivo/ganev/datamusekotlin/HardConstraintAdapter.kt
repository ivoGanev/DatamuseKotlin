package com.ivo.ganev.datamusekotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView

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
