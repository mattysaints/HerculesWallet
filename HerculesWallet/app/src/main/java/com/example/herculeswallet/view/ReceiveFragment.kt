package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.herculeswallet.R
import com.example.herculeswallet.utils.Encryption
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.firebase.database.collection.LLRBNode
import com.google.zxing.WriterException


class ReceiveFragment : Fragment() {

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

        val viewPager2 = view.findViewById<ViewPager2>(R.id.pager)
        val pageAdapter = PageAdapter(parentFragmentManager,lifecycle,model.getUserData().value!!.preferences)
        viewPager2.adapter = pageAdapter

        model.userMutableLiveData.observe(viewLifecycleOwner){
            pageAdapter.setList(it.preferences)
        }

        super.onViewCreated(view, savedInstanceState)
    }




}