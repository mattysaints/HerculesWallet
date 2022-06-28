package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.repository.AuthenticationRepository
import com.example.herculeswallet.utils.Encryption
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SendFragment : Fragment(R.layout.fragment_send){

    private val model: MainViewModel by activityViewModels()
    private val encryption : Encryption = Encryption()
    private var database: DatabaseReference = Firebase.database.getReference("Users")
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

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
        var user = model.getUserData().value!! as User

        val quantity_send : EditText = view.findViewById(R.id.quantity_send)
        val address_receiver : EditText = view.findViewById(R.id.address_receiver)
        val list_crypto : AutoCompleteTextView = view.findViewById(R.id.list_crypto)

        var crypto_owned : ArrayList<String> = ArrayList()

        val adapterText =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_dropdown_item_1line, user.preferences as ArrayList<String>) }

        list_crypto.setAdapter(adapterText)

        val md5_address = model.getUserData().value?.email?.let { encryption.md5(it + "/"+ list_crypto.text) }

        //ottenere quantity asset del wallet
        //var qnty_crypto : Double = user.wallet.get(md5_address)!!.quantity_user!!.toDouble()

        super.onViewCreated(view, savedInstanceState)
    }

}

/*
* if(qnty_crypto >= quantity_send.text.toString().toDouble()){
            //esegui transazione
            val temp = qnty_crypto - quantity_send.text.toString().toDouble()
            user.wallet.get(md5_address)!!.quantity_user = temp
            database.child(firebaseAuth.currentUser!!.uid).setValue(user)
        } else {
            Toast.makeText(activity,"Non hai fondi sufficienti",Toast.LENGTH_SHORT)
        }*/