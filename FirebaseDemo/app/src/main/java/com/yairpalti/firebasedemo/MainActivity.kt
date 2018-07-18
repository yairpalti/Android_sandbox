package com.yairpalti.firebasedemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var firebaseAnalytics: FirebaseAnalytics? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var flow:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // FirebaseAnalytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        // Authentication
        firebaseAuth = FirebaseAuth.getInstance()
        flow = "onCreate"
    }

    fun buLoginEvent(@Suppress("UNUSED_PARAMETER") view: View) {

        loginToFireBase(etEmail.text.toString(), etPassword.text.toString())
    }


    private fun loginToFireBase(email: String, password: String) {
        if (etEmail.text.isEmpty()) {
            firebaseAuth!!.signInAnonymously().addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Successful login", Toast.LENGTH_LONG).show()

                    val currentUser = firebaseAuth!!.currentUser
                    //save in database
                    Log.d("Login:", currentUser!!.uid)
                } else {
                    Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            firebaseAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Successful login", Toast.LENGTH_LONG).show()

                            val currentUser = firebaseAuth!!.currentUser
                            //save in database
                            Log.d("Login:", currentUser!!.uid)
                        } else {
                            Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }

                    }
        }

    }

    override fun onRestart() {
        super.onRestart()
        flow = "onRestart"
    }
    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth!!.currentUser
        if (flow == "onRestart") {
            firebaseAuth!!.signOut()
        }
        if (currentUser != null) {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)
//            finish()
        }

    }
}
