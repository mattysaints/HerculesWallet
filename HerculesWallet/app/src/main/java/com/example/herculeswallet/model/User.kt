package com.example.herculeswallet.model

import java.util.prefs.AbstractPreferences

class User (val email: String, val password: String, var wallet: ArrayList<Crypto>, var preferences: ArrayList<String>){

    override fun toString(): String {
        return "User(email='$email', password='$password', wallet=$wallet, preferences=$preferences)"
    }
}