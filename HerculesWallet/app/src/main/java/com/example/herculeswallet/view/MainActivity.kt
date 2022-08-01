package com.example.herculeswallet.view

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.herculeswallet.R
import com.example.herculeswallet.databinding.ActivityMainBinding
import com.example.herculeswallet.model.Crypto
import com.example.herculeswallet.model.User
import com.example.herculeswallet.viewmodels.MainViewModel
import dmax.dialog.SpotsDialog


class MainActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityMainBinding
    private val model : MainViewModel by viewModels()
    //private lateinit var googleSignInClient: GoogleSignInClient
    //private lateinit var firebaseAuth: FirebaseAuth

    //constant
    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()!!.hide()
        setFullscreen()

        model.getCryptoList() //CHIAMATA API

        //Preparo i dialogs
        val dialog = SpotsDialog.Builder()
            .setContext(this@MainActivity)
            .setMessage(getString(R.string.message_login) + " ...")
            .setCancelable(false)
            .build()

        val errore = AlertDialog.Builder(this)
        errore.setTitle(getString(R.string.error_login))
        errore.setMessage(getString(R.string.check_log))
        errore.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id -> })

        model.userMutableLiveData.observe(this,
            Observer<User?> { user ->
                if (user != null) {
                    dialog.show()
                    model.cryptoListLiveData.observe(this, Observer<List<Crypto>>{
                        Crypto -> if (Crypto.isNotEmpty()){
                        startActivity(Intent(this, Wallet::class.java))
                    }
                    })
                }
            })

        binding.buttonAccedi.setOnClickListener {
            if(binding.email.text.toString().isNotEmpty() && binding.password.text.toString().isNotEmpty()) {
                var posted = false
                val message = getString(R.string.message_login)
                dialog.setMessage("$message ...")
                dialog.show()
                model.login(binding.email.text.toString(), binding.password.text.toString())
                model.success.observe(this, Observer<Boolean> { login ->
                    if (login == false && !posted) {
                        model.setsuccess(true)
                        dialog.dismiss()
                        errore.setMessage(getString(R.string.error_credentials))
                        errore.show()
                        posted = true;
                    }
                })
            }else{
                errore.show()
            }
        }

        binding.buttonRegistrati.setOnClickListener {
            if(binding.email.text.toString().isNotEmpty() && binding.password.text.toString().isNotEmpty()) {
                var posted = false
            dialog.setMessage(getString(R.string.registration) + " ...")
            dialog.show()
            model.register(binding.email.text.toString(),binding.password.text.toString())
                model.success.observe(this, Observer<Boolean> { login ->
                    if (login == false && !posted) {
                        model.setsuccess(true)
                        dialog.dismiss()
                        errore.setTitle(getString(R.string.error_registration))
                        errore.setMessage(model.getexceptionMutableLiveData().value.toString())
                        errore.show()
                        posted = true;
                    }
                })
            }else{
                errore.show()
            }
        }

    }

    override fun onBackPressed(){
        return
    }

    fun setFullscreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
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