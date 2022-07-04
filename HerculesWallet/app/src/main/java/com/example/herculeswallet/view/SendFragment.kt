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
    private var receiver_id : String = ""

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
            CoroutineScope(Dispatchers.IO).launch { getReceiverId(address_receiver.text.toString(),list_crypto.text.toString()) }

            val crypto = Klaxon().parse<Crypto>(user.wallet.get(md5_address)!!.toJson())
            val qnty_crypto = crypto?.quantity_user!!.toDouble()

            if(qnty_crypto >= quantity_send.text.toString().toDouble()){
                val temp_quantity = qnty_crypto - quantity_send.text.toString().toDouble()
                CoroutineScope(Dispatchers.IO).launch {
                    if (md5_address != null && receiver_id.isNotEmpty()) {
                        database.child(receiver_id).get().addOnSuccessListener {
                            val qReceiver = it.child("wallet").child(address_receiver.text.toString()).child("quantity_user").getValue()
                            database.child(receiver_id).child("wallet").child(address_receiver.text.toString()).child("quantity_user").setValue(
                                quantity_send.text.toString().toDouble() + qReceiver.toString().toDouble()
                            )
                            database.child(firebaseAuth.currentUser!!.uid).child("wallet").child(md5_address).child("quantity_user").setValue(temp_quantity)
                        }.addOnFailureListener {
                            makeToast(false)
                        }
                    } else {
                        makeToast(false)
                    }
                }
            } else {
                Toast.makeText(view.context,"Non hai fondi sufficienti",Toast.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun makeToast(success: Boolean){
        if (!success){
            Looper.prepare()
            Toast.makeText(view!!.context, "Indirizzo Errato", Toast.LENGTH_SHORT).show()
            Looper.loop()
        }
    }

    fun getReceiverId(address: String, crypto: String){
        database.get().addOnSuccessListener { i ->
            val utenti : HashMap<String,User> = HashMap()
            for (temp : DataSnapshot in i.children.toMutableSet()){
                temp.key?.let { temp.value.toString().parseJson(User::class)
                    ?.let { it1 -> utenti.put(it, it1) } }
            }
            for ((key, value) in utenti) {
                val address_in_db = encryption.md5("${value.email}/$crypto")
                receiver_id = if(value.wallet.containsKey(address) && address.equals(address_in_db)){
                    key
                } else {
                    ""
                }
            }
        }
    }

}