package com.example.herculeswallet.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.herculeswallet.model.User

class MainViewModel : ViewModel() {

    lateinit var users : MutableLiveData<User>
    lateinit var current_user : LiveData<User>


}