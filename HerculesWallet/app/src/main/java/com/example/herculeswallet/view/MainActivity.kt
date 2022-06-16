package com.example.herculeswallet.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.herculeswallet.R
import com.example.herculeswallet.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    //constants
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //Configure Google Sign
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        //checkUser()

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //  you will get result here in result.data
                Log.d(TAG, "onActivityResult: Google SignIn")
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    //Google SignIn success, now auth with firebase
                    val account = accountTask.getResult(ApiException::class.java)
                    firebaseAuthWithGoogleAccount(account)

                } catch (e: Exception) {
                    //failed Google SignIn
                    Log.d(TAG,"onActivityResult: ${e.message}")
                }
            }

        }

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        val button_accedi = findViewById<Button>(R.id.button_accedi)
        button_accedi.setOnClickListener {
            firebaseAuthWithEmailAndPassword(email.text.toString(),password.text.toString())
        }

        val facebook = findViewById<ImageView>(R.id.facebook)
        facebook.setOnClickListener {

        }

        //Google SignInButton
        val google = findViewById<ImageView>(R.id.google)
        google.setOnClickListener {
            Log.d(TAG, "OnCreate: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startForResult.launch(intent)
        }

    }

    private fun firebaseAuthWithEmailAndPassword(email: String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = firebaseAuth.currentUser
                    Toast.makeText(this,"Accesso riuscitomatti",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Wallet::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin fireabse auth with google account")
        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //login success
                Log.d(TAG,"firebaseAuthWithGoogleAccount: LoggedIn")

                //get LoggedIn user
                val firebaseUser = firebaseAuth.currentUser
                //get user info
                val uid = firebaseAuth!!.uid
                val email = firebaseUser!!.email

                Log.d(TAG,"firebaseAuthWithGoogleAccount: Email: $email")

                //check if user is new or existing
                if(authResult.additionalUserInfo!!.isNewUser){
                    //user is new - Account created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created ..... \n$email")
                    Toast.makeText(this,"Account creato con successo",Toast.LENGTH_SHORT).show()
                } else {
                    //existing user - LoggedIn
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing user ..... \n$email")
                    Toast.makeText(this,"Accesso riuscito",Toast.LENGTH_SHORT).show()
                }

                //start wallet activity
                startActivity(Intent(this, Wallet::class.java))
                finish()

            }.addOnFailureListener { e ->
                Log.d(TAG,"firebaseAuthWithGoogleAccount: Loggin Failed ")
                Toast.makeText(this,"Accesso fallito",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //check if user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user is already logged in
            //start wallet activity
            Toast.makeText(this,"Autenticato: \n${firebaseUser.email}",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Wallet::class.java))
            finish()
        }
    }

}