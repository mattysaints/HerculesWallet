package com.example.herculeswallet.repository

import android.content.BroadcastReceiver
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.utils.Encryption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tomatobean.jsonparser.parseJson

object DatabaseRepository {

    private var database: DatabaseReference = Firebase.database.getReference("Users")
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val encryption : Encryption = Encryption()
    private val receiver_id : String = ""

    fun getReceiverId(address: String, crypto: String) : String{
        var receiver_id : String = ""
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
        return receiver_id
    }

    fun transactionWalletUser(receiver_id : String, address_receiver: String, address_sender : String, send_quantity: String, new_quantity: String) : Boolean{
        database.child(receiver_id).get().addOnSuccessListener {
            val qReceiver = it.child("wallet").child(address_receiver).child("quantity_user").getValue()
            database.child(receiver_id).child("wallet").child(address_receiver).child("quantity_user").setValue(
                send_quantity.toDouble() + qReceiver.toString().toDouble()
            )
            database.child(firebaseAuth.currentUser!!.uid).child("wallet").child(address_sender).child("quantity_user").setValue(new_quantity)
        }.addOnFailureListener {
            return@addOnFailureListener
        }
        return true
    }

    fun removeCryptoToWallet(crypto: Crypto, user_info : User) {
        val temp = mutableListOf<Crypto>()
        for ((keys,value) in user_info.wallet){
            if(!value.name.equals(crypto.name)){
                temp.add(value)
            }
        }
        encryption.md5(firebaseAuth.currentUser!!.email.toString() + "/" + crypto.name)
            ?.let { database.child(firebaseAuth.currentUser!!.uid).child("wallet").setValue(temp) }
    }

    fun addCryptoToWallet(crypto: Crypto){
        encryption.md5(firebaseAuth.currentUser!!.email.toString() + "/" + crypto.name)
            ?.let { database.child(firebaseAuth.currentUser!!.uid).child("wallet").child(it).setValue(crypto) }
    }

    fun getUserFromDB(user_id : String): User? {
        val utenti : HashMap<String,User> = HashMap()
        database.get().addOnSuccessListener { i ->
            for (temp : DataSnapshot in i.children.toMutableSet()){
                temp.key?.let { temp.value.toString().parseJson(User::class)
                    ?.let { it1 -> utenti.put(it, it1) } }
            }
        }
        return utenti.get(user_id)
    }


}