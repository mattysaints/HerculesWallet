package com.example.herculeswallet.view

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class CryptoListFragment : Fragment(){

    private var layoutManager: RecyclerView.LayoutManager? = null
    private val model: MainViewModel by activityViewModels()
    private var tempArrayList : List<Crypto> = mutableListOf<Crypto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_crypto_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listCrypto = view.findViewById<RecyclerView>(R.id.list_crypto)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_circular)
        val searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listCrypto.layoutManager = layoutManager
        val adapter = RecyclerViewAdapter(model.getUserData().value!!.preferences)
        listCrypto.adapter = adapter

        model.getCryptoList()

        model.cryptoListLiveData.observe(viewLifecycleOwner){
            progressBar.visibility = View.INVISIBLE
            tempArrayList = it
            adapter.setList(it)
        }

        model.userMutableLiveData.observe(viewLifecycleOwner){
            adapter.setFavourite(it.preferences)
        }

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredList = mutableListOf<Crypto>()
                for (crypto : Crypto in tempArrayList){
                    if(crypto.name.lowercase().contains(query!!.lowercase())){
                        filteredList.add(crypto)
                    }
                }
                adapter.setList(filteredList)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = mutableListOf<Crypto>()
                for (crypto : Crypto in tempArrayList){
                    if(crypto.name.lowercase().contains(newText!!.lowercase())){
                        filteredList.add(crypto)
                    }
                }
                adapter.setList(filteredList)
                return false
            }
        })

    }

}