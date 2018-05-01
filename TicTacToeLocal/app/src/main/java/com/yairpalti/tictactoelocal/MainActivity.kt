package com.yairpalti.tictactoelocal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    protected fun buttonClick(view: View) {
        val buttonSelected = view as Button
        var cellID = 0
        when (buttonSelected.id) {
            R.id.button1 -> cellID=1
            R.id.button2 -> cellID=2
            R.id.button3 -> cellID=3
            R.id.button4 -> cellID=4
            R.id.button5 -> cellID=5
            R.id.button6 -> cellID=6
            R.id.button7 -> cellID=7
            R.id.button8 -> cellID=8
            R.id.button9 -> cellID=9
        }
        Toast.makeText(this, "ID:"+cellID, Toast.LENGTH_LONG).show()
    }
}
