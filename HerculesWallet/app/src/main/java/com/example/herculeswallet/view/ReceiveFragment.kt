package com.example.herculeswallet.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.herculeswallet.R
import com.example.herculeswallet.utils.Encryption
import com.example.herculeswallet.viewmodels.MainViewModel
import com.google.firebase.database.collection.LLRBNode
import com.google.zxing.WriterException


class ReceiveFragment : Fragment(R.layout.fragment_receive) {

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

        val addressEditText : TextView = view.findViewById(R.id.address_crypto)
        val md5_address = model.getUserData().value?.email?.let { encryption.md5(it+ "/bitcoin") }
        addressEditText.text = md5_address

        val qrgEncoder = QRGEncoder(md5_address, null, QRGContents.Type.TEXT, 512)

        try {
            // Getting QR-Code as Bitmap
            val bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            val addressQr : ImageView = view.findViewById(R.id.address_qr)
            addressQr.setImageBitmap(bitmap)
        } catch (e : WriterException){

        }
        super.onViewCreated(view, savedInstanceState)
    }


}