package com.example.herculeswallet.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.herculeswallet.R
import com.example.herculeswallet.databinding.WalletBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Wallet : AppCompatActivity() {

    private lateinit var binding: WalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide();
        setContentView(R.layout.wallet)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        val navController = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.findNavController()

        navController?.let { bottomNavigationView.setupWithNavController(it) }
    }
}

