package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
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
       //"recycler view delle crypto dell'utente"
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list_crypto)
        recyclerView.layoutManager = layoutManager
        val adapter = WalletRWA()
        recyclerView.adapter = adapter

        var user = model.userMutableLiveData.value
        var repo = model.cryptoListLiveData.value

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if(mapToListCrypto(user!!.wallet)[position].asset_id.equals("BTC").not()){
                    model.removeCryptoFromWallet(mapToListCrypto(user!!.wallet)[position])
                } else {
                    adapter.setList(mapToListCrypto(user!!.wallet))
                    Toast.makeText(context,"Azione non consentita",Toast.LENGTH_SHORT).show()
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        model.userMutableLiveData.observe(viewLifecycleOwner){
            //From Map<String,Crypto> to List<Crypto>
            var wallet = mutableListOf<Crypto>()
            for (entry in it.wallet.toMap().entries.iterator()) {
                val crypto = Klaxon().parse<Crypto>(entry.value.toJson())
                val crypto_repo = (repo?.filter { it.name == crypto!!.name })?.first()
                crypto!!.price_usd = (String.format("%.3f", crypto_repo?.price_usd)).toDouble()
                crypto.logo_url = crypto_repo?.logo_url.toString()
                wallet.add(crypto)
            }
            adapter.setList(wallet)
        }

        model.userMutableLiveData.observe(viewLifecycleOwner){
            user = it
        }

        model.cryptoListLiveData.observe(viewLifecycleOwner){
            repo = it
        }

    }

    fun mapToListCrypto(wallet: Map<String,Crypto>) : List<Crypto>{
        val temp = mutableListOf<Crypto>()
        for ((keys,value) in wallet){
            value.setPrice((String.format("%.3f", value.price_usd)).toDouble())
            temp.add(value)
        }
        return temp
    }


}