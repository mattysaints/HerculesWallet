package com.example.herculeswallet.repository

import androidx.lifecycle.MutableLiveData
import com.beust.klaxon.JsonObject
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.utils.Encryption
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.json.JSONObject
import tomatobean.jsonparser.parseJson
import tomatobean.jsonparser.toJson


object AuthenticationRepository {

    private var userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var utentewalletMutableLiveData: MutableLiveData<User> = MutableLiveData<User>()
    private var firebaseAuth: FirebaseAuth
    private var database: DatabaseReference
    private val encryption : Encryption = Encryption()

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

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get User object and use the values to update the UI
                val user = dataSnapshot.child(firebaseAuth.currentUser!!.uid).value.toString().parseJson(User::class)
                utentewalletMutableLiveData.postValue(User(user!!.email, user.wallet,user.preferences))
                println("Aggiornamento : " + user.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        database.addValueEventListener(userListener)

    }



    fun login(email: String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser

                    database.child(firebaseAuth.uid.toString()).get().addOnSuccessListener {
                        val email : String = it.child("email").getValue(String::class.java).toString()
                        val preferences : ArrayList<String> = it.child("preferences").getValue() as ArrayList<String>
                        val wallet = it.child("wallet").getValue() as HashMap<String,Crypto>
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
                    var hashMap = HashMap<String,Crypto>()
                    var crypto : Crypto = Crypto("Bitcoin","1234",30.000,"prova.url",0.005)
                    hashMap.put("fb69aeb6c434160fc4b846383c535de7",crypto)
                    val utente : User = User(user?.email.toString(), hashMap, arrayListOf("Bitcoin","Ripple"))
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