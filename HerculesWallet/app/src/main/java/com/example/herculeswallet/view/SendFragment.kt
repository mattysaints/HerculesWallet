package com.example.herculeswallet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.beust.klaxon.Klaxon
import com.example.herculeswallet.R
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.utils.Encryption
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
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
        val scanner_qr : ImageButton = view.findViewById(R.id.scanner)
        var listCrypto = mutableListOf<String>()

        model.userMutableLiveData.observe(viewLifecycleOwner){
            user = it
        }

        val barcodeLauncher = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            if (result.contents == null) {
                Toast.makeText(view.context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                address_receiver.setText(result.contents)
            }
        }

        scanner_qr.setOnClickListener(View.OnClickListener {
            val options : ScanOptions = ScanOptions()
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.captureActivity = CaptureAct::class.java
            barcodeLauncher.launch(options)
        })

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

        model.getisDone().observe(viewLifecycleOwner){
            if(it!=null){
                if(it){
                    Snackbar.make(view,getString(R.string.success_send_crypto), Snackbar.LENGTH_LONG).show()
                    model.getisDone().value = null
                } else {
                    Toast.makeText(view.context, getString(R.string.error_address), Toast.LENGTH_SHORT).show()
                    model.getisDone().value = null
                }
            }
        }

        action_button.setOnClickListener { view ->
            if(list_crypto.text.toString().isNotEmpty()){
               if(quantity_send.text.toString().isNotEmpty() && quantity_send.text.toString().toDouble() > 0) {
                   if (address_receiver.text.toString().isNotEmpty()) {
                       val md5_address =
                           user.email.let { encryption.md5(it + "/" + list_crypto.text.toString()) }
                       if (!md5_address.equals(address_receiver.text.toString())) {
                           val crypto =
                               Klaxon().parse<Crypto>(user.wallet.get(md5_address)!!.toJson())
                           val qnty_crypto = crypto?.quantity_user!!.toDouble()
                           if (qnty_crypto >= quantity_send.text.toString().toDouble()) {
                               model.transactionWalletUser(
                                   address_receiver.text.toString(),
                                   md5_address!!,
                                   quantity_send.text.toString(),
                                   list_crypto.text.toString()
                               )
                           } else {
                               Toast.makeText(
                                   view.context,
                                   getString(R.string.not_enough_balance),
                                   Toast.LENGTH_SHORT
                               )
                                   .show()
                           }
                       } else {
                           Toast.makeText(
                               view.context,
                               getString(R.string.error_address),
                               Toast.LENGTH_SHORT
                           )
                               .show()
                       }
                   } else {
                       Toast.makeText(
                           view.context,
                           getString(R.string.message_info_address),
                           Toast.LENGTH_SHORT
                       )
                           .show()
                   }
               } else {
                   Toast.makeText(view.context, getString(R.string.message_info_qty), Toast.LENGTH_SHORT)
                       .show()
               }
            } else {
                Toast.makeText(view.context, getString(R.string.message_info_selectC), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

}