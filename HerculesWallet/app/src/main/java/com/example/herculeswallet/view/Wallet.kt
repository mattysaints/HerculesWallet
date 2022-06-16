package com.example.herculeswallet.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.herculeswallet.R
import com.example.herculeswallet.databinding.WalletBinding

class Wallet : AppCompatActivity() {

    private lateinit var binding: WalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()!!.hide();
        binding = WalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container_view, HomeFragment())
        fragmentTransaction.commit()

        /*binding.bottomNavigatinView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeNav -> {
                    Toast.makeText(this,"Test",Toast.LENGTH_SHORT)
                    return@setOnItemSelectedListener true
                }
                else -> {false}
            }
        }*/


    }
}