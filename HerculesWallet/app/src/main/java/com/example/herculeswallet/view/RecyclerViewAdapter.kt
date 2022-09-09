package com.example.herculeswallet.view

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class RecyclerViewAdapter(private val model: MainViewModel) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    private var crypto_list = mutableListOf<Crypto>()
    private lateinit var context : Context
    private lateinit var v : View
    private var preferences = model.getUserData().value!!.preferences.toMutableList()
    private var wallet = mapToListString(model.getUserData().value!!.wallet).toMutableList()


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageView: AppCompatImageView
        val name_crypto: TextView
        var price_crypto: TextView
        val favourite: ImageButton
        val action : ImageButton

        init {
            imageView = itemView.findViewById(R.id.image_crypto)
            name_crypto = itemView.findViewById(R.id.name_crypto)
            price_crypto = itemView.findViewById(R.id.price_crypto)
            favourite = itemView.findViewById(R.id.favourite)
            action = itemView.findViewById(R.id.action_crypto)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        v = LayoutInflater.from(parent.context).inflate(R.layout.crypto_list_item,parent,false)
        context = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name_crypto.text = crypto_list[position].name.replace("\\s".toRegex(), " ")
        if(crypto_list[position].price_usd.toString().length>8){
            holder.price_crypto.text = "$ " + crypto_list[position].price_usd.toString().substring(0,8) } else { holder.price_crypto.text =  "$ " + crypto_list[position].price_usd.toString()}
        if(crypto_list[position].logo_url.isNotEmpty()){
            Picasso.get()
                .load(Uri.parse(crypto_list[position].logo_url))
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_placeholder)
                .resize(64,64)
                .into(holder.imageView)
        }

        if(preferences.contains(crypto_list[position].asset_id)){
            holder.favourite.setBackgroundResource(R.drawable.ic_baseline_turned_in_24)
        } else{
            holder.favourite.setBackgroundResource(R.drawable.ic_baseline_turned_in_not_24)
        }

        if(wallet.contains(crypto_list[position].name)){
            holder.action.setBackgroundResource(R.drawable.ic_baseline_delete_24)
        } else{
            holder.action.setBackgroundResource(R.drawable.ic_baseline_add_24)
        }


        holder.favourite.setOnClickListener(View.OnClickListener {
            if(preferences.contains(crypto_list[position].asset_id) && preferences.size!=1 && crypto_list[position].asset_id.equals("BTC").not()){
                preferences.remove(crypto_list[position].asset_id)
                model.setPreferences(preferences)
                Toast.makeText(context,v.resources.getString(R.string.crypto_removed_favs), Toast.LENGTH_SHORT).show()
            } else if (! preferences.contains(crypto_list[position].asset_id)){
                preferences.add(crypto_list[position].asset_id)
                model.setPreferences(preferences)
                Toast.makeText(context,v.resources.getString(R.string.crypto_added_favs), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,v.resources.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show()
            }
        })

        holder.action.setOnClickListener(View.OnClickListener {
            if(wallet.contains(crypto_list[position].name) && wallet.size!=1 && crypto_list[position].asset_id.equals("BTC").not()){
                wallet.remove(crypto_list[position].name)
                model.removeCryptoFromWallet(crypto_list[position])
                Toast.makeText(context,v.resources.getString(R.string.crypto_removed_wallet), Toast.LENGTH_SHORT).show()
            } else if (!wallet.contains(crypto_list[position].name)){
                wallet.add(crypto_list[position].name)
                model.addCryptoToWallet(crypto_list[position])
                Toast.makeText(context,v.resources.getString(R.string.crypto_added_wallet), Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(context,v.resources.getString(R.string.action_not_allowed), Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun getItemCount(): Int {
        return crypto_list.size
    }

    fun setList(list: List<Crypto>) {
        this.crypto_list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun setFavourite(preferences: List<String>){
        this.preferences = preferences.toMutableList()
        notifyDataSetChanged()
    }

    fun mapToListString(wallet: Map<String,Crypto>) : List<String>{
        val temp = mutableListOf<String>()
        for ((keys,value) in wallet){
            temp.add(value.name)
        }
        return temp
    }

}