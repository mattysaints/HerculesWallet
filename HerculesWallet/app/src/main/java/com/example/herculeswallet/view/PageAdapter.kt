package com.example.herculeswallet.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.herculeswallet.model.Crypto

class PageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, listC: MutableList<String>) : FragmentStateAdapter(fragmentManager,lifecycle){

    private var list = listC

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return CryptoPage(list.get(position))
    }

    fun setList(list: List<String>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

}