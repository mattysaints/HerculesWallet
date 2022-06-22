package com.example.herculeswallet.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class AuthenticationRepository() {

    private var userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var utentewalletMutableLiveData: MutableLiveData<User> = MutableLiveData<User>()
    private var firebaseAuth: FirebaseAuth
    private var database: DatabaseReference

    //constants
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        if (firebaseAuth.getCurrentUser() != null){
            database.child(firebaseAuth.uid.toString()).get().addOnSuccessListener {
                val email : String = it.child("email").getValue(String::class.java).toString()
                val preferences : List<String> = it.child("preferences").getValue() as List<String>
                val wallet : HashMap<String,Crypto> = it.child("wallet").getValue() as HashMap<String, Crypto>
                utentewalletMutableLiveData.postValue(User(email,wallet, preferences))
            }
        }
    }

    fun login(email: String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser

                    database.child(firebaseAuth.uid.toString()).get().addOnSuccessListener {
                        val email : String = it.child("email").getValue(String::class.java).toString()
                        val preferences : List<String> = it.child("preferences").getValue() as List<String>
                        val wallet : HashMap<String,Crypto> = it.child("wallet").getValue() as HashMap<String, Crypto>
                        utentewalletMutableLiveData.postValue(User(email,wallet, preferences))
                    }

                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }

    fun register(email: String, pass: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    //Create user in realtime database
                    var hashMap : HashMap<String, Crypto> = HashMap<String, Crypto> ()
                    var crypto : Crypto = Crypto("Bitcoin","1234",30000,"prova.url")
                    hashMap.put("6f2825a9b7abe88e6d5b819e48c3a19d",crypto)
                    val utente : User = User(user?.email.toString(), hashMap, arrayListOf("BTC"))
                    val userId = firebaseAuth.uid
                    database.child(userId.toString()).setValue(utente)

                    utentewalletMutableLiveData!!.postValue(utente)

                } else {
                    //Toast.makeText(application, task.exception?.message, Toast.LENGTH_SHORT) .show()
                }
            })
    }

    fun signOut() {
        firebaseAuth.signOut()
        userLoggedMutableLiveData!!.postValue(true)
    }


    fun getUserLoggedMutableLiveData(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }

    fun getUserWallet(): MutableLiveData<User> {
        return utentewalletMutableLiveData
    }


}

/*
fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //get LoggedIn user
                val firebaseUser = firebaseAuth.currentUser
                //get user info
                val uid = firebaseAuth!!.uid
                val email = firebaseUser!!.email

                //check if user is new or existing
                if(authResult.additionalUserInfo!!.isNewUser){
                    //user is new - Account created

                } else {
                    //existing user - LoggedIn

                }


            }
    }
 */