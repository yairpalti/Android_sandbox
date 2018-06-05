package com.yairpalti.tictactoelocal

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var game:Game = Game
    private var rowSize = 3
    private var winSize = 3

    // Extension function to Int
    private fun Int.ptToPx():Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, this.toFloat(), resources.displayMetrics).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createGameView()
    }
    private fun createGameView() {
        val buttonSize:Int = 40 - (rowSize-3)*5
        val gameSize = rowSize*rowSize
        game.gameSize = gameSize
        game.winSize = winSize
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
                button.setBackgroundColor(Color.GRAY)
                button.tag = (tag++).toString()
                var buttonParams = TableRow.LayoutParams(buttonSize.ptToPx(), buttonSize.ptToPx())
                buttonParams.rightMargin = 3.ptToPx()
                button.layoutParams = buttonParams
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

                // Set game cell clickListener
                val gameCellClickListener = View.OnClickListener { view -> buttonClick(view) }
                button.setOnClickListener(gameCellClickListener)
                tableRow.addView(button)
                // Adding all buttons to the game
                game.addButton(button);
            }
        }
    }
    private fun resetGame() {
        gameLayout.removeAllViews()
        game.resetButtons()
    }


    fun winSizeClick(view: View) {
//        val buttonSelected = view as RadioButton
//        if (buttonSelected.isChecked) {
//            winSize = if (buttonSelected.tag.toString() == "N") rowSize else rowSize-1
//            game.winSize = winSize
//        }
        setWinSize()
    }
    fun gameSizeClick(view: View) {
        val buttonSelected = view as RadioButton
        if (buttonSelected.isChecked) {
            rowSize = buttonSelected.tag.toString().toInt()
            setWinSize()
            resetGame()
            createGameView()
        }
    }

    private fun setWinSize() {
        // Set text
        radioButtonN.text = rowSize.toString()
        radioButtonNMinus1.text = (rowSize-1).toString()
        // Handle special case of rowSize 3
        if (rowSize == 3) {
            radioButtonNMinus1.isEnabled = false
            radioButtonNMinus1.isChecked = false
            radioButtonN.isChecked = true
        } else
            radioButtonNMinus1.isEnabled = true
        // Set values
        winSize = if (radioButtonN.isChecked) rowSize else rowSize-1
        game.winSize = winSize
    }

    private fun enableSetting(enable:Boolean) {
 //       radioGroup.isEnabled = enable
        radioButton6.isEnabled = enable
        radioButton5.isEnabled = enable
        radioButton4.isEnabled = enable
        radioButtonN.isEnabled = enable
        if (rowSize == 3)
            radioButtonNMinus1.isEnabled = false
        else
            radioButtonNMinus1.isEnabled = enable

    }
    private fun buttonClick(view: View) {
        val buttonSelected = view as Button
        var cellID = buttonSelected.tag.toString().toInt()
        val gameStatus = game.playGame(cellID, buttonSelected)
        val gameOver:Boolean = (gameStatus.status != Game.GameStatuses.GAME_ON)
        if (gameOver) {
            Toast.makeText(this, gameStatus.endGameMessage, Toast.LENGTH_SHORT).show()
            // Disable buttons
            game.enableButtons (false)
            // reset game after 2 seconds
            val handler = Handler()
            handler.postDelayed({ game.reset() }, 2000)
        }
        enableSetting(gameOver)
    }


}