package com.example.herculeswallet.model

import java.util.prefs.AbstractPreferences

data class User (val email: String, var wallet: Map<String,Crypto>, var preferences: List<String>){

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

    override fun toString(): String {
        return "User(email='$email', wallet=$wallet, preferences=$preferences)"
    }


}