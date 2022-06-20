package com.example.herculeswallet.model

import java.util.prefs.AbstractPreferences

data class User (val email: String, val password: String, var wallet: HashMap<Crypto,String>, var preferences: ArrayList<String>){

    override fun toString(): String {
        return "User(email='$email', password='$password', wallet=$wallet, preferences=$preferences)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (wallet != other.wallet) return false

        return true
    }

    override fun hashCode(): Int {
        return wallet.hashCode()
    }


}