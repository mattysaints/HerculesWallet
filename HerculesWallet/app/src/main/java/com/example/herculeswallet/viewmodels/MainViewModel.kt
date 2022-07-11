package com.example.herculeswallet.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.repository.AuthenticationRepository
import com.example.herculeswallet.repository.CryptoRepository
import com.example.herculeswallet.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel() : ViewModel() {

    val authRepo: AuthenticationRepository = AuthenticationRepository
    val cryptoRepo: CryptoRepository = CryptoRepository
    val DBRepo : DatabaseRepository = DatabaseRepository
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
        return authRepo.getUserWallet()
    }

    fun getCrypto(): MutableLiveData<List<Crypto>> {
        return cryptoRepo.getCryptoList()
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

    fun getIcon(): HashMap<String, String> {
        viewModelScope.launch(Dispatchers.IO) {
            cryptoRepo.getCryptoIconRequest()
        }
        return cryptoRepo.getCryptoIcon()
    }

    fun transactionWalletUser(address_receiver: String, address_sender : String, send_quantity: String,crypto: String) : Boolean{
        var isDone = false
        viewModelScope.launch (Dispatchers.IO){
            isDone = DBRepo.transactionWalletUser(userMutableLiveData.value!!,address_receiver,address_sender,send_quantity,crypto)
        }
        return isDone
    }

    fun addCryptoToWallet(crypto: Crypto){
        DBRepo.addCryptoToWallet(crypto)
    }

    fun removeCryptoFromWallet(crypto: Crypto){
        DBRepo.removeCryptoToWallet(crypto,userMutableLiveData.value!!)
    }

    fun setPreferences(preferences: List<String>){
        DBRepo.setPreferences(preferences)
    }

}