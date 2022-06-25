package com.example.herculeswallet.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RecyclerViewAdapter() : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    private var crypto_list = mutableListOf<Crypto>()
    private lateinit var context : Context

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageView: AppCompatImageView
        val name_crypto: TextView
        var price_crypto: TextView

        init {
            imageView = itemView.findViewById(R.id.image_crypto)
            name_crypto = itemView.findViewById(R.id.name_crypto)
            price_crypto = itemView.findViewById(R.id.price_crypto)
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
        if(crypto_list[position].logo_url.length>0){
            Picasso.get().load(Uri.parse(crypto_list[position].logo_url)).resize(64,64).into(holder.imageView)
        } else {/*immagine non fornita*/}
    }

    override fun getItemCount(): Int {
        return crypto_list.size
    }

    fun setList(list: List<Crypto>) {
        println("Test" + list.get(0).logo_url)
        this.crypto_list = list.toMutableList()
        notifyDataSetChanged()
    }


}