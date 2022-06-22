package com.example.herculeswallet.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.herculeswallet.model.User
import com.example.herculeswallet.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseUser


class MainViewModel() : ViewModel() {

    val repo : AuthenticationRepository = AuthenticationRepository()
    var userMutableLiveData : MutableLiveData<User>
    var loggedStatus: MutableLiveData<Boolean>

    init {
        userMutableLiveData = repo.getUserWallet()
        loggedStatus = repo.getUserLoggedMutableLiveData()
    }

    fun login(email: String, password: String){
        repo.login(email,password)
    }

    fun register(email: String, password: String){
        repo.register(email,password)
    }

    fun signOut() {
        repo.signOut()
    }

    fun getUserData(): MutableLiveData<User> {
        return userMutableLiveData
    }

    fun getLogStatus(): MutableLiveData<Boolean> {
        return loggedStatus
    }

}