package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.viewmodels.MainViewModel

class CryptoListFragment : Fragment(R.layout.fragment_crypto_list){

    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var model: MainViewModel
    private val viewModel by viewModels<MainViewModel>()

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
        super.onViewCreated(view, savedInstanceState)

        val listCrypto = view.findViewById<RecyclerView>(R.id.list_crypto)

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        listCrypto.layoutManager = layoutManager
        var adapter = RecyclerViewAdapter()
        listCrypto.adapter = adapter

        viewModel.cryptoListLiveData.observe(viewLifecycleOwner,
            Observer<List<Crypto>> { list ->
                println("Crypto :$list")
            })
    }


}