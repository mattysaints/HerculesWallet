package com.example.herculeswallet.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.beust.klaxon.Klaxon
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.utils.Encryption
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tomatobean.jsonparser.toJson


class SendFragment : Fragment(R.layout.fragment_send){

    private val model: MainViewModel by activityViewModels()
    private val encryption : Encryption = Encryption()

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

        model.userMutableLiveData.observe(viewLifecycleOwner){
            user = it
        }

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

            if(quantity_send.text.toString().isNotEmpty()
                && address_receiver.text.toString().isNotEmpty()
                && list_crypto.text.toString().isNotEmpty()) {

                if (qnty_crypto >= quantity_send.text.toString().toDouble()) {
                    var transaction: Boolean? = null
                    model.transactionWalletUser(
                        address_receiver.text.toString(),
                        md5_address!!,
                        quantity_send.text.toString(),
                        list_crypto.text.toString()
                    )

                    val handler = Handler()
                    handler.postDelayed({
                        // do something after 4000ms
                        transaction = model.getisDone()
                        if (transaction == true) {
                            Toast.makeText(view.context, "Invio Riuscito", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(view.context, "Indirizzo Errato", Toast.LENGTH_SHORT).show()
                        }
                    }, 4000)
                } else {
                    Toast.makeText(view.context, "Non hai fondi sufficienti", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(view.context, "Uno o più campi non sono stati compilati", Toast.LENGTH_SHORT).show()
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }

}