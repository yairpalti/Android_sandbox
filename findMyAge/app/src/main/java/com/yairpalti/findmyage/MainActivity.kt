package com.yairpalti.findmyage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        getMyAgeButton.setOnClickListener {
        } */
    }
    fun findMyAgeButtonOnClick(view:View) {
        // fire when button is clicked
        val yearOfBirth:Int = yearTV.text.toString().toInt()
        val age = Calendar.getInstance().get(Calendar.YEAR) - yearOfBirth
        ageTV.text = age.toString()
    }
}
