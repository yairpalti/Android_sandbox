package com.yairpalti.tictactoelocal

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import java.util.*

class Game(private val context: Context)  {
 //   TODO: Change Game to be singleton (but context can't be static)
 // lateinit var context: Context
 //   var gameSize:Int = 0
    var gameSize:Int = 0
    private val player1 = Player("You", "X", Color.MAGENTA, PlayerType.HUMAN)
    private val player2 = Player("Android", "O", Color.CYAN, PlayerType.ANDROID)
    private var activePlayer = player1
    private var nextPlayer = player2
    private var gameButtons = ArrayList<Button>()

    fun addButton(button: Button) {
        gameButtons.add(button)
    }
    fun playGame(cellId: Int, buttonSelected: Button) {
        // play with the active player
        buttonSelected.text = activePlayer.symbol
        buttonSelected.setBackgroundColor(activePlayer.color)
        activePlayer.play(cellId)
        // update board
        buttonSelected.isEnabled = false   // Disable button press
        // check game over/winner
        var gameOverMessage: String? = checkWinner()
        if ((gameOverMessage == null)  && (player1.numberOfPlays() + player2.numberOfPlays() == gameSize))
            gameOverMessage = "No winner"
        if (gameOverMessage != null) {
            gameOver(gameOverMessage)
        } else {
            // continue to next player
            updateNextPlayer()
            if (activePlayer.playerType == PlayerType.ANDROID) {
                val handler = Handler()
                handler.postDelayed({ autoPlay() }, 500)
            }
        }
    }
    private fun enableButtons (on:Boolean, alsoReset:Boolean = false) {
        for (button in gameButtons) {
            button.isEnabled = on
            if (alsoReset) {
                button.setBackgroundColor(Color.GRAY)
                button.text = ""
            }
        }
    }
    private fun resetGame() {
        player1.restartGame()
        player2.restartGame()
        activePlayer = player1
        nextPlayer = player2
        enableButtons(true, true)
    }
    private fun gameOver(gameOverMessage:String) {
        // Show message
        Toast.makeText(context, gameOverMessage, Toast.LENGTH_SHORT).show()
        // Disable buttons
        enableButtons (false)
        // reset game after 2 seconds
        val handler = Handler()
        handler.postDelayed({ resetGame() }, 2000)
    }


    private fun updateNextPlayer() {
        val currPlayer = activePlayer
        activePlayer = nextPlayer
        nextPlayer = currPlayer
    }

    private fun autoPlay()  {
        var emptyCells = ArrayList<Int>()
        for (cellId in 1..gameSize) {
            if (!(player1.containsCell(cellId) || player2.containsCell(cellId)))
                emptyCells.add(cellId)
        }
        // TODO("unit test that we don't reach this method with no emptyCells")
        // TODO("add logic to find best cell to play")
        // Find if there is a cell that will enable winning
        var cellsOnLine = activePlayer.isWinInNextPlay(gameSize, emptyCells)
        var cellId = cellsOnLine.cellId
        if (cellsOnLine.status == Player.CellsOnLineStatus.NO_WIN) {
            // Find if there is critical cell for next player
            cellsOnLine = nextPlayer.isWinInNextPlay(gameSize, emptyCells)
            cellId = cellsOnLine.cellId
            if (cellsOnLine.status == Player.CellsOnLineStatus.NO_WIN) {
                // random cell
                val r = Random()
                val randomIndex = r.nextInt(emptyCells.size - 0) + 0
                cellId = emptyCells[randomIndex]
            }
        }
        var buSelected: Button = gameButtons[0]
        for (button in gameButtons)
            if (button.tag.toString().toInt() == cellId) {
                buSelected = button
                break
            }
        playGame(cellId, buSelected)
    }
    private fun checkWinner(): String? {
        if (activePlayer.isWin(gameSize)) {
            return activePlayer.name + " Won"
        }
        return null
    }
}