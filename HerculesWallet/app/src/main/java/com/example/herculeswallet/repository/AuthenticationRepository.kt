package com.example.herculeswallet.repository

import androidx.lifecycle.MutableLiveData
import com.beust.klaxon.Klaxon
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.utils.Encryption
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import tomatobean.jsonparser.parseJson
import tomatobean.jsonparser.toJson


object AuthenticationRepository{

    private var userLoggedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var utentewalletMutableLiveData: MutableLiveData<User> = MutableLiveData<User>()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val encryption : Encryption = Encryption()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
    private var success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var exceptionMutableLiveData: MutableLiveData<String> = MutableLiveData<String>()
    private val cryptoRepo: CryptoRepository = CryptoRepository

    init {
        if (firebaseAuth.getCurrentUser() != null){
            database.child(firebaseAuth.uid.toString()).get().addOnSuccessListener {
                try {
                    val email: String = it.child("email").getValue(String::class.java).toString()
                    val preferences: List<String> =
                        it.child("preferences").getValue() as List<String>
                    val wallet: HashMap<String, Crypto> =
                        it.child("wallet").getValue() as HashMap<String, Crypto>
                    utentewalletMutableLiveData.postValue(User(email, wallet, preferences))
                    setListenerDatabase()
                } catch (e: NullPointerException){
                    signOut()
                }
            }
        }
    }


    fun setListenerDatabase(){
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get User object and use the values to update the UI
                val user = Klaxon().parse<User>(dataSnapshot.child(firebaseAuth.currentUser!!.uid).value!!.toJson())
                utentewalletMutableLiveData.postValue(User(user!!.email, user.wallet,user.preferences))
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

                    database.child(user!!.uid).get().addOnSuccessListener {
                        val email : String = it.child("email").getValue(String::class.java).toString()
                        val preferences : ArrayList<String> = it.child("preferences").getValue() as ArrayList<String>
                        val wallet = it.child("wallet").getValue() as HashMap<String,Crypto>
                        utentewalletMutableLiveData.postValue(User(email,wallet, preferences))
                        this.success.postValue(true)
                        setListenerDatabase()
                    }

                } else {
                    //error
                    this.success.postValue(false)
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
                    //Get BTC from API to initialize wallet
                    for (cryptoAPI in cryptoRepo.getCryptoList().value!!){
                        if (cryptoAPI.asset_id.equals("BTC")){
                            encryption.md5(user!!.email+ "/Bitcoin")?.let { hashMap.put(it,cryptoAPI) }
                        }
                    }
                    val utente : User = User(user?.email.toString(), hashMap, arrayListOf("BTC"))
                    database.child(firebaseAuth.uid.toString()).setValue(utente)
                    utentewalletMutableLiveData.postValue(utente)
                    this.success.postValue(true)
                    setListenerDatabase()

                } else {
                    this.exceptionMutableLiveData.postValue(task.exception?.message.toString())
                    this.success.postValue(false)
                }
            })
    }

    fun setsuccess(value: Boolean){
        this.success.postValue(value)
    }

    fun getsuccess(): MutableLiveData<Boolean>{
        return this.success
    }

    fun getexceptionMutableLiveData(): MutableLiveData<String>{
        return this.exceptionMutableLiveData
    }

    fun signOut() {
        firebaseAuth.signOut()
        userLoggedMutableLiveData.postValue(true)
    }

    fun getUserLoggedMutableLiveData(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }

    fun getUserWallet(): MutableLiveData<User> {
        return utentewalletMutableLiveData
    }

}

/*
aaaa
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