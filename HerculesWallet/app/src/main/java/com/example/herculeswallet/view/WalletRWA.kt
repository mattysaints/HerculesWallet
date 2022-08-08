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

class WalletRWA: RecyclerView.Adapter<WalletRWA.ViewHolder>() {
    private var wallet: Array<Crypto>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletRWA.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: WalletRWA.ViewHolder, position: Int) {
        Picasso.get()
            .load(Uri.parse(wallet!![position].logo_url.toString()))
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
            .resize(64,64)
            .into(holder.itemLogo)

        holder.itemName.text = wallet!![position].name
        holder.itemQnty.text = wallet!![position].quantity_user.toString()
        holder.itemPrice.text = "$ " + wallet!![position].price_usd.toString()

    }

    override fun getItemCount(): Int {
        return wallet!!.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemName : TextView
        var itemQnty: TextView
        var itemPrice: TextView
        var itemLogo: AppCompatImageView
        init{
            itemName = itemView.findViewById(R.id.name_crypto)
            itemQnty = itemView.findViewById(R.id.quantity_crypto)
            itemPrice = itemView.findViewById(R.id.price_crypto)
            itemLogo = itemView.findViewById(R.id.image_crypto)
        }
    }

    fun setList(list: List<Crypto>) {
        this.wallet = list.toTypedArray()
        notifyDataSetChanged()
    }

}