package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.viewmodels.MainViewModel


class FavsFragment : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<FavRecyclerViewAdapter.ViewHolder>? = null
    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.fav_crypto)
        recyclerView.layoutManager = layoutManager
        adapter = FavRecyclerViewAdapter()
        recyclerView.adapter = adapter

        //From List<String> to List<Crypto>
        val user = model.getUserData().value
        var favs: List<Crypto> = emptyList()
        if(user != null){
            val iterator = user!!.preferences.listIterator()
            while (iterator.hasNext()) {
                val repo = model.cryptoListLiveData.value
                val asset = iterator.next()
                val crypto_repo = repo?.filter { it.asset_id == asset }?.firstOrNull()
                var crypto: Crypto = Crypto(
                    asset,
                    "",
                    (String.format("%.3f", crypto_repo?.price_usd)).toDouble(),
                    crypto_repo?.logo_url.toString(),
                    0.0
                )
                favs += crypto
            }
        }

        model.userMutableLiveData.observe(viewLifecycleOwner){
            (adapter as FavRecyclerViewAdapter).setList(favs)
        }
    }

}