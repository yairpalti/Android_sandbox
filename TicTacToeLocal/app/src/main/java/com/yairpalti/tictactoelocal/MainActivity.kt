package com.yairpalti.tictactoelocal

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class Player(val ID:Int=0, val symbol:String, val color:Int) { //TODO - add properties - player type:Android/human
    private var playedCells = ArrayList<Int>()
    fun play(cellId: Int) {
        playedCells.add(cellId)
    }
    fun containsCell(cellId: Int):Boolean {
        return playedCells.contains(cellId)
    }
    fun restartGame() {
        playedCells.clear()
    }

    fun isWin(): Boolean { //TODO - check logic in loops
        if ((playedCells.contains(1) && playedCells.contains(2) && playedCells.contains(3)) ||
                (playedCells.contains(4) && playedCells.contains(5) && playedCells.contains(6)) ||
                (playedCells.contains(7) && playedCells.contains(8) && playedCells.contains(9)) ||
                // columns
                (playedCells.contains(1) && playedCells.contains(4) && playedCells.contains(7)) ||
                (playedCells.contains(2) && playedCells.contains(5) && playedCells.contains(8)) ||
                (playedCells.contains(3) && playedCells.contains(6) && playedCells.contains(9)) ||
                // diagonal
                (playedCells.contains(1) && playedCells.contains(5) && playedCells.contains(9)) ||
                (playedCells.contains(3) && playedCells.contains(5) && playedCells.contains(7)))
            return true
        return false
    }
}
class MainActivity : AppCompatActivity() {

    private val player1 = Player(1,"X", Color.MAGENTA)
    private val player2 = Player(2, "O", Color.CYAN)
    private var buttonsPressed = ArrayList<Button>()
    private var activePlayer = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    protected fun buttonClick(view: View) { // TODO - refactor to extract cellID from String
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
    private fun resetGame() {
        Thread.sleep(2000)
        player1.restartGame()
        player2.restartGame()
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
        if (activePlayer == 1) { // TODO - get rid of the if/else by moving those to be properties in Player
            buttonSelected.text = player1.symbol
            buttonSelected.setBackgroundColor(player1.color)
            player1.play(cellId)
            activePlayer = 2
            val continueGame = autoPlay()
            if (!continueGame) {
                resetGame()
            }
        } else {
            buttonSelected.text = player2.symbol
            buttonSelected.setBackgroundColor(player2.color)
            player2.play(cellId)
            activePlayer = 1
        }
        // Disable button press
        buttonSelected.isEnabled = false
        // Add selected button to list
        buttonsPressed.add(buttonSelected)
        // check winner
        // TODO - move board logic to new class Board with statuses
        // TODO - move size of the board to be N
        if (checkWinner() || buttonsPressed.size == 9) {
            resetGame()
        }
    }
    private fun autoPlay() : Boolean { // TODO - add logic to find best cell to play
        var emptyCells = ArrayList<Int>()
        for (cellId in 1..9) {
            if (!(player1.containsCell(cellId) || player2.containsCell(cellId)))
                emptyCells.add(cellId)
        }
        if (emptyCells.isEmpty())
            return false
        // random cell
        val r = Random()
        val randomIndex = r.nextInt(emptyCells.size-0)+0
        val cellId = emptyCells[randomIndex]
        var buSelected:Button = button1
        when (cellId) { //TODO - set buSelected by converting the Int to button name
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
        if (player1.isWin()) {
            winner = 1
        } else if (player2.isWin()) {
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