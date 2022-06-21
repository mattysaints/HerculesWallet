package com.example.herculeswallet.repository

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class AuthenticationRepository {

    private val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser>? = null
    private val userLoggedMutableLiveData: MutableLiveData<Boolean>? = null
    private lateinit var firebaseAuth: FirebaseAuth

    fun firebaseAuthWithEmailAndPassword(email: String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = firebaseAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //login success

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

            }.addOnFailureListener { e ->

            }
    }




}