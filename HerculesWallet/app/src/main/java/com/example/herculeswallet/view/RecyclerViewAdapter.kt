package com.example.herculeswallet.view

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.squareup.picasso.Picasso


class RecyclerViewAdapter(preferences: List<String>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    private var crypto_list = mutableListOf<Crypto>()
    private lateinit var context : Context
    private var preferences = preferences

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageView: AppCompatImageView
        val name_crypto: TextView
        var price_crypto: TextView
        val favourite: ImageButton

        init {
            imageView = itemView.findViewById(R.id.image_crypto)
            name_crypto = itemView.findViewById(R.id.name_crypto)
            price_crypto = itemView.findViewById(R.id.price_crypto)
            favourite = itemView.findViewById(R.id.favourite)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.crypto_list_item,parent,false)
        context = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name_crypto.text = crypto_list[position].name.replace("\\s".toRegex(), " ")
        if(crypto_list[position].price_usd.toString().length>8){
            holder.price_crypto.text = "$ " + crypto_list[position].price_usd.toString().substring(0,8) } else { holder.price_crypto.text =  "$ " + crypto_list[position].price_usd.toString()}
        if(crypto_list[position].logo_url.toString().isNotEmpty()){
            Picasso.get()
                .load(Uri.parse(crypto_list[position].logo_url.toString()))
                .placeholder(R.drawable.logo2)
                .error(R.drawable.logo2)
                .resize(64,64)
                .into(holder.imageView)
        }

        if(preferences.contains(crypto_list[position].name)){
            println("Preferito: " + crypto_list[position].name)
            holder.favourite.setBackgroundResource(R.drawable.ic_baseline_turned_in_24)
        } else{
            holder.favourite.setBackgroundResource(R.drawable.ic_baseline_turned_in_not_24)
        }

    }

    override fun getItemCount(): Int {
        return crypto_list.size
    }

    fun setList(list: List<Crypto>) {
        this.crypto_list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun setFavourite(preferences: List<String>){
        this.preferences = preferences
        notifyDataSetChanged()
    }

}