package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.utils.Encryption
import com.example.herculeswallet.viewmodels.MainViewModel
import tomatobean.jsonparser.parseJson
import tomatobean.jsonparser.toJson


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<FavRecyclerViewAdapter.ViewHolder>? = null
    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val walletText : TextView = view.findViewById(R.id.userwallet)
        var user = model.getUserData().value!!
        val enc : Encryption = Encryption()
        val list_crypto = "Bitcoin"
        val md5_address = model.getUserData().value?.email?.let { enc.md5(it + "/"+ list_crypto) }
        val crypto = Klaxon().parse<Crypto>(user.wallet.get(md5_address)!!.toJson())
        val qnty_crypto = crypto?.quantity_user!!.toDouble()
        val priceUSD = crypto?.price_usd!!.toDouble()
        var totalUSD: Double = qnty_crypto * priceUSD
        if(totalUSD.equals(0)) walletText.text = "0" else walletText.text = totalUSD.toString()

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fav_crypto)
        recyclerView.layoutManager = layoutManager
        adapter = FavRecyclerViewAdapter()
        recyclerView.adapter = adapter

        model.userMutableLiveData.observe(viewLifecycleOwner){
            (adapter as FavRecyclerViewAdapter).setList(model.userMutableLiveData.value!!.preferences)
        }


    }

}