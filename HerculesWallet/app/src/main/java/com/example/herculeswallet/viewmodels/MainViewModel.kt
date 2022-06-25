package com.example.herculeswallet.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.repository.AuthenticationRepository
import com.example.herculeswallet.repository.CryptoRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel() : ViewModel() {

    val authRepo: AuthenticationRepository = AuthenticationRepository()
    val cryptoRepo: CryptoRepository = CryptoRepository()
    var userMutableLiveData: MutableLiveData<User>
    var loggedStatus: MutableLiveData<Boolean>
    var cryptoListLiveData: MutableLiveData<List<Crypto>>


    init {
        userMutableLiveData = authRepo.getUserWallet()
        loggedStatus = authRepo.getUserLoggedMutableLiveData()
        cryptoListLiveData = cryptoRepo.getCryptoList()
    }

    fun login(email: String, password: String) {
        authRepo.login(email, password)
    }

    fun register(email: String, password: String) {
        authRepo.register(email, password)
    }

    fun signOut() {
        authRepo.signOut()
    }

    fun getUserData(): MutableLiveData<User> {
        return userMutableLiveData
    }

    fun getLogStatus(): MutableLiveData<Boolean> {
        return loggedStatus
    }

    fun getCryptoList() {
        viewModelScope.launch(Dispatchers.IO) {
            cryptoRepo.getCryptoIconRequest()
            cryptoRepo.getCryptoListRequest()
        }
    }

}