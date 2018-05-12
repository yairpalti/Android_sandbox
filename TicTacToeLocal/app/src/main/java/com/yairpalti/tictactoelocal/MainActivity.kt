package com.yairpalti.tictactoelocal

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    protected fun buttonClick(view: View) {
        val buttonSelected = view as Button
        var cellID = 0
        when (buttonSelected.id) {
            R.id.button1 -> cellID = 1
            R.id.button2 -> cellID = 2
            R.id.button3 -> cellID = 3
            R.id.button4 -> cellID = 4
            R.id.button5 -> cellID = 5
            R.id.button6 -> cellID = 6
            R.id.button7 -> cellID = 7
            R.id.button8 -> cellID = 8
            R.id.button9 -> cellID = 9
        }
 //       Toast.makeText(this, "ID:" + cellID, Toast.LENGTH_SHORT).show()
        playGame(cellID, buttonSelected)
    }

    private var player1 = ArrayList<Int>()
    private var player2 = ArrayList<Int>()
    private var buttonsPressed = ArrayList<Button>()
    private var activePlayer = 1

    private fun resetGame() {
        Thread.sleep(2000)
        player1.clear()
        player2.clear()
        activePlayer = 1
        for (button in buttonsPressed) {
            button.text = ""
            button.setBackgroundColor(Color.GRAY)
            button.isEnabled = true
        }
        buttonsPressed.clear()
    }
    private fun playGame(cellId: Int, buttonSelected: Button) {
        // update players
        if (activePlayer == 1) {
            buttonSelected.text = "X"
            buttonSelected.setBackgroundColor(Color.MAGENTA)
            player1.add(cellId)
            activePlayer = 2
            val continueGame = autoPlay()
            if (!continueGame) {
                resetGame()
            }
        } else {
            buttonSelected.text = "O"
            buttonSelected.setBackgroundColor(Color.CYAN)
            player2.add(cellId)
            activePlayer = 1
        }
        // Disable button press
        buttonSelected.isEnabled = false
        // Add selected button to list
        buttonsPressed.add(buttonSelected)
        // check winner
        if (checkWinner()) {
            resetGame()
        }
    }
    private fun autoPlay() : Boolean {
        var emptyCells = ArrayList<Int>()
        for (cellId in 1..9) {
            if (!(player1.contains(cellId) || player2.contains(cellId)))
                emptyCells.add(cellId)
        }
        if (emptyCells.isEmpty())
            return false
        // random cell
        val r = Random()
        val randomIndex = r.nextInt(emptyCells.size-0)+0
        val cellId = emptyCells[randomIndex]
        var buSelected:Button = button1
        when (cellId) {
            1-> buSelected=button1
            2-> buSelected=button2
            3-> buSelected=button3
            4-> buSelected=button4
            5-> buSelected=button5
            6-> buSelected=button6
            7-> buSelected=button7
            8-> buSelected=button8
            9-> buSelected=button9
        }
        playGame(cellId, buSelected)
        return true
    }
    private fun checkWinner(): Boolean {
        var winner = -1
        // rows
        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) ||
                (player1.contains(4) && player1.contains(5) && player1.contains(6)) ||
                (player1.contains(7) && player1.contains(8) && player1.contains(9)) ||
                // columns
                (player1.contains(1) && player1.contains(4) && player1.contains(7)) ||
                (player1.contains(2) && player1.contains(5) && player1.contains(8)) ||
                (player1.contains(3) && player1.contains(6) && player1.contains(9)) ||
                // diagonal
                (player1.contains(1) && player1.contains(5) && player1.contains(9)) ||
                (player1.contains(3) && player1.contains(5) && player1.contains(7))) {
            winner = 1
        } else if ((player2.contains(1) && player2.contains(2) && player2.contains(3)) ||
                (player2.contains(4) && player2.contains(5) && player2.contains(6)) ||
                (player2.contains(7) && player2.contains(8) && player2.contains(9)) ||
                // columns
                (player2.contains(1) && player2.contains(4) && player2.contains(7)) ||
                (player2.contains(2) && player2.contains(5) && player2.contains(8)) ||
                (player2.contains(3) && player2.contains(6) && player2.contains(9)) ||
                // diagonal
                (player2.contains(1) && player2.contains(5) && player2.contains(9)) ||
                (player2.contains(3) && player2.contains(5) && player2.contains(7))) {
            winner = 2
        }
        if (winner != -1) {
            val winnerStr:String = if (winner==1) "Player 1" else "Player2"
            Toast.makeText(this, winnerStr + " Won", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }

}