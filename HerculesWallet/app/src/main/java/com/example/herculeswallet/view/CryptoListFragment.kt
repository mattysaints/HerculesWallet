package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class CryptoListFragment : Fragment(){

    private var layoutManager: RecyclerView.LayoutManager? = null
    private val model: MainViewModel by activityViewModels()

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

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listCrypto.layoutManager = layoutManager
        val adapter = RecyclerViewAdapter(model.getUserData().value!!.preferences)
        listCrypto.adapter = adapter

        model.getCryptoList()

        model.cryptoListLiveData.observe(viewLifecycleOwner){
            progressBar.visibility = View.INVISIBLE
            adapter.setList(it)
        }

        model.userMutableLiveData.observe(viewLifecycleOwner){
            adapter.setFavourite(it.preferences)
        }

    }


}