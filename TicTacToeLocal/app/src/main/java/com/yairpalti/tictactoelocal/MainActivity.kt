package com.yairpalti.tictactoelocal

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.view.ViewGroup
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var game:Game = Game(this)
    // Extension function to Int
    private fun Int.ptToPx():Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, this.toFloat(), resources.displayMetrics).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createGameView()
    }
    private fun createGameView() {
        val rowSize:Int = 3
        val buttonSize:Int = 40 - (rowSize-3)*5
        val gameSize = rowSize*rowSize
        game.gameSize = gameSize
        var tag = 1
        for (i in 1..rowSize) {
            // Create TableRow
            var tableRow = TableRow(this)
            var tableRowParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT)
            tableRowParams.bottomMargin = 3.ptToPx()
            tableRow.layoutParams = tableRowParams
            tableRow.gravity = Gravity.CENTER
            gameLayout.addView(tableRow)
            // Create cells (buttons)
            for (j in 1..rowSize) {
                var button = Button(this);
                // button.id = button9 "@+id/button9"
                button.setBackgroundColor(Color.GRAY)
                button.tag = (tag++).toString()
                var buttonParams = TableRow.LayoutParams(buttonSize.ptToPx(), buttonSize.ptToPx())
//                var buttonParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
                buttonParams.rightMargin = 3.ptToPx()
                button.layoutParams = buttonParams
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

                // Set game cell clickListener
                val gameCellclickListener = View.OnClickListener { view -> buttonClick(view) }
                button.setOnClickListener(gameCellclickListener)
                tableRow.addView(button)
                // Adding all buttons to the game
                game.addButton(button);
            }
        }
    }

    private fun buttonClick(view: View) {
        val buttonSelected = view as Button
        var cellID = buttonSelected.tag.toString().toInt()
        game.playGame(cellID, buttonSelected)
    }


}