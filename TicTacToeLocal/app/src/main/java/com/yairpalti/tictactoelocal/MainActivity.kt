package com.yairpalti.tictactoelocal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {
    var game:Game = Game(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Add all buttons of an array
        for (i in 0 until gameLayout.childCount) {
            val child = gameLayout.getChildAt(i)
            if (child is ViewGroup)
                for (j in 0 until child.childCount) {
                    val childButton = child.getChildAt(j)
                    if (childButton is Button)
                        game.addButton(childButton)
                }
        }
        // TODO - create all buttons within the kotlin code (to allow size depended board)
        // https://stackoverflow.com/questions/15741520/adding-a-button-for-android-in-java-code
        // https://www.techotopia.com/index.php/Creating_an_Android_User_Interface_in_Java_Code_using_Android_Studio
    }

    protected fun buttonClick(view: View) { // TODO - refactor to extract cellID from String
        val buttonSelected = view as Button
        var cellID = buttonSelected.tag.toString().toInt()
 //       Toast.makeText(this, "ID:" + cellID, Toast.LENGTH_SHORT).show()
        game.playGame(cellID, buttonSelected)
    }


}