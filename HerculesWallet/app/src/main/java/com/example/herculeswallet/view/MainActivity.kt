package com.example.herculeswallet.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.herculeswallet.databinding.ActivityMainBinding
import com.example.herculeswallet.model.User
import com.example.herculeswallet.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityMainBinding
    private val model : MainViewModel by viewModels()
    //private lateinit var googleSignInClient: GoogleSignInClient
    //private lateinit var firebaseAuth: FirebaseAuth

    //constants
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()!!.hide();
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN

        model.userMutableLiveData.observe(this,
            Observer<User?> { user ->
                if (user != null) {
                    startActivity(Intent(this, Wallet::class.java))
                }
            })

        binding.buttonAccedi.setOnClickListener {
            model.login(binding.email.text.toString(),binding.password.text.toString())
        }

        binding.buttonRegistrati.setOnClickListener {
            model.register(binding.email.text.toString(),binding.password.text.toString())
        }

    }

    override fun onBackPressed(){
        return
    }

}

/*val facebook = findViewById<ImageView>(R.id.facebook)
        facebook.setOnClickListener {

        }

        //Google SignInButton
        val google = findViewById<ImageView>(R.id.google)
        google.setOnClickListener {
            Log.d(TAG, "OnCreate: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startForResult.launch(intent)
        }


        //Configure Google Sign
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("6624234530-71bkunkn5a2njl8mejfahpq9ruk05d42.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //checkUser()

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                //  you will get result here in result.data
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    //Google SignIn success, now auth with firebase
                    val account = accountTask.getResult(ApiException::class.java)
                    model.google(account)
                } catch (e: Exception) {
                    //failed Google SignIn
                }
            }
        }*/