package com.example.herculeswallet.view

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.beust.klaxon.Klaxon
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.utils.Encryption
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tomatobean.jsonparser.parseJson
import tomatobean.jsonparser.toJson


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

        var user = model.userMutableLiveData.value!!
        val quantity_send : EditText = view.findViewById(R.id.quantity_send)
        val address_receiver : EditText = view.findViewById(R.id.address_receiver)
        val list_crypto : AutoCompleteTextView = view.findViewById(R.id.list_crypto)
        val action_button : FloatingActionButton = view.findViewById(R.id.fab_send)
        var listCrypto = mutableListOf<String>()

        for (item in user.wallet){
            listCrypto.add(item.value.name)
        }
        val adapterText =
            activity?.let {
                CustomArrayAdapter(it,listCrypto)
            }

        list_crypto.setAdapter(adapterText)

        model.userMutableLiveData.observe(viewLifecycleOwner){
            listCrypto = mutableListOf()
            for (item in it.wallet){
                listCrypto.add(item.value.name)
            }
            adapterText!!.setList(listCrypto)
        }

        action_button.setOnClickListener { view ->
            val md5_address = user.email.let { encryption.md5(it + "/"+ list_crypto.text.toString()) }
            val crypto = Klaxon().parse<Crypto>(user.wallet.get(md5_address)!!.toJson())
            val qnty_crypto = crypto?.quantity_user!!.toDouble()

            if(qnty_crypto >= quantity_send.text.toString().toDouble()){
                if (md5_address != null &&
                    model.transactionWalletUser(address_receiver.text.toString(),md5_address,quantity_send.text.toString(),list_crypto.text.toString())) {
                    Toast.makeText(view.context,"Invio Riuscito",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(view.context,"Indirizzo Errato",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(view.context,"Non hai fondi sufficienti",Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

}