package com.example.herculeswallet.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class OptionsFragment : Fragment(R.layout.fragment_options) {
    private val model : MainViewModel by viewModels()

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

        val user: TextView = view.findViewById(R.id.user)
        user.text = model.userMutableLiveData.value!!.email
        val bottone: Button = view.findViewById(R.id.buttonlogout)
        bottone.setOnClickListener(){
            onDestroy()
            model.signOut()
            model.userMutableLiveData.value = null
            startActivity(Intent(context, MainActivity::class.java))

        }
    }

}