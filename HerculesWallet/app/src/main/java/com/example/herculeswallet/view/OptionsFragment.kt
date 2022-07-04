package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.herculeswallet.R
import com.example.herculeswallet.databinding.FragmentOptionsBinding
import com.example.herculeswallet.model.User
import com.example.herculeswallet.viewmodels.MainViewModel


class OptionsFragment : Fragment(R.layout.fragment_options) {

    private lateinit var binding: FragmentOptionsBinding
    private val model : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOptionsBinding.inflate(layoutInflater)
        binding.buttonlogout.setOnClickListener() {
            println("Hola")
            model.signOut()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.userMutableLiveData.observe(viewLifecycleOwner,
            Observer<User?> { user ->
                if (user == null) {
                    onStop()
                }
            })
    }

}