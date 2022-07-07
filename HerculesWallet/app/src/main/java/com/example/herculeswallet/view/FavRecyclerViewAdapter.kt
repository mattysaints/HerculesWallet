package com.example.herculeswallet.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.squareup.picasso.Picasso


class FavRecyclerViewAdapter : RecyclerView.Adapter<FavRecyclerViewAdapter.ViewHolder>(){
    private var favs: Array<Crypto>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavRecyclerViewAdapter.ViewHolder, position: Int) {
        Picasso.get()
            .load(Uri.parse(favs!![position].logo_url.toString()))
            .placeholder(R.drawable.logo2)
            .error(R.drawable.logo2)
            .resize(64,64)
            .into(holder.itemLogo)

        holder.itemName.text = favs!![position].name
        holder.itemPrice.text = "Valore:"
        holder.qnty.text = "$ " + favs!![position].price_usd.toString()

    }

    override fun getItemCount(): Int {
        return favs!!.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemName : TextView
        var itemPrice: TextView
        var itemLogo: AppCompatImageView
        var qnty: TextView
        init{
            itemName = itemView.findViewById(R.id.name_crypto)
            itemPrice = itemView.findViewById(R.id.price_crypto)
            itemLogo = itemView.findViewById(R.id.image_crypto)
            qnty = itemView.findViewById(R.id.quantity_crypto)
        }
    }

    fun setList(list: List<Crypto>) {
        this.favs = list.toTypedArray()
        notifyDataSetChanged()
    }
}