package com.example.herculeswallet.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.herculeswallet.R
import com.example.herculeswallet.model.User
import com.example.herculeswallet.viewmodels.MainViewModel


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
        model.userMutableLiveData.observe(viewLifecycleOwner,
            Observer<User?> { user ->
                if (user == null) {
                    onStop()
                }
            })

        val bottone: Button = view.findViewById(R.id.buttonlogout)
        bottone.setOnClickListener(){
            onDestroy()
            model.signOut()
            model.userMutableLiveData.value = null
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

}