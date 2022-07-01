package com.example.herculeswallet.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.herculeswallet.model.Crypto

class CustomArrayAdapter(context: Context, var items : List<String>) : ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,items) {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): String? {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }

    fun setList(list: List<String>) {
        this.items = list.toMutableList()
        notifyDataSetChanged()
    }

}