package com.example.herculeswallet.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.herculeswallet.R
import com.example.herculeswallet.databinding.ActivityMainBinding
import com.example.herculeswallet.model.User
import com.example.herculeswallet.viewmodels.MainViewModel


class OptionsFragment : Fragment(R.layout.fragment_options) {

    private lateinit var binding: ActivityMainBinding
    private val model : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        model.userMutableLiveData.observe(this,
            Observer<User?> { user ->
                if (user == null) {
                    onStop()
                    //startActivity(Intent(this, MainActivity::class.java))
                }
            })

        binding.buttonAccedi.setOnClickListener {
            model.signOut()
        }
    }

}