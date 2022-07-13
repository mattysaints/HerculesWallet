package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.herculeswallet.R
import com.example.herculeswallet.viewmodels.MainViewModel


class ReceiveFragment : Fragment(R.layout.fragment_receive) {

    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_receive, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var listCrypto = mutableListOf<String>()
        val pageAdapter = PageAdapter(childFragmentManager,lifecycle,listCrypto)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.pager)

        for (item in model.userMutableLiveData.value!!.wallet){
            listCrypto.add(item.value.name)
        }
        viewPager2.adapter = pageAdapter

        model.userMutableLiveData.observe(viewLifecycleOwner){
            listCrypto = mutableListOf()
            for (item in model.userMutableLiveData.value!!.wallet){
                listCrypto.add(item.value.name)
            }
            val newPA = PageAdapter(childFragmentManager,lifecycle,listCrypto)
            newPA.setList(listCrypto)
            viewPager2.adapter = newPA
        }





        super.onViewCreated(view, savedInstanceState)
    }




}