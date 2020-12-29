package com.ivo.ganev.datamusekotlin

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.ivo.ganev.datamusekotlin.core.HardConstraint

class SpinnerAdapterHardConstraint(
    private val context: Context,
    private val hardConstraint: List<String>
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
        text?.text = hardConstraint[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}
