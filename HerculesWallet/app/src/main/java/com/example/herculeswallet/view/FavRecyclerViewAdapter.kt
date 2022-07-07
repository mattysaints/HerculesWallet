package com.example.herculeswallet.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R


class FavRecyclerViewAdapter : RecyclerView.Adapter<FavRecyclerViewAdapter.ViewHolder>(){
    private var favs: Array<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.itemName.text = favs!![position].toString()
    }

    override fun getItemCount(): Int {
        return favs!!.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemName : TextView
        init{
            itemName = itemView.findViewById(R.id.fav_title)
        }
    }
    fun setList(list: List<String>) {
        this.favs = list.toTypedArray()
        notifyDataSetChanged()
    }
}