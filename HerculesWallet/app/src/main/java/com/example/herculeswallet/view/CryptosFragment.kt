package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.viewmodels.MainViewModel
import tomatobean.jsonparser.toJson

class CryptosFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<WalletRWA.ViewHolder>? = null
    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_cryptos, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list_crypto)
        recyclerView.layoutManager = layoutManager
        adapter = WalletRWA()
        recyclerView.adapter = adapter

        //From Map<String,Crypto> to List<Crypto>
        val user = model.getUserData().value
        var wallet: List<Crypto> = emptyList()
        for (entry in user!!.wallet.toMap().entries.iterator()) {
            val crypto = Klaxon().parse<Crypto>(entry.value.toJson())
            val repo = model.cryptoListLiveData.value
            val crypto_repo = (repo?.filter { it.name == crypto!!.name })?.first()
            crypto!!.price_usd = (String.format("%.3f", crypto_repo?.price_usd)).toDouble()
            crypto!!.logo_url = crypto_repo?.logo_url.toString()
            wallet += crypto
        }

        model.userMutableLiveData.observe(viewLifecycleOwner){
            (adapter as WalletRWA).setList(wallet)
        }
    }
}