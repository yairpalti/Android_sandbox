package com.yairpalti.firebasedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        var bundle:Bundle=intent.extras
        val email= bundle.getString("email")
        val uid= bundle.getString("uid")
        Toast.makeText(applicationContext, "Already login with user email: $email\n UID: $uid",
                Toast.LENGTH_LONG).show()
    }
}
