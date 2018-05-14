package com.yairpalti.tictactoelocal

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.view.ViewGroup
import kotlin.math.sqrt


enum class PlayerType {
    HUMAN, ANDROID
}

class Player(val name:String, val symbol:String, val color:Int, val playerType:PlayerType=PlayerType.HUMAN) {
    private var playedCells = ArrayList<Int>()
    fun play(cellId: Int) {
        playedCells.add(cellId)
    }
    fun numberOfPlays():Int {
        return playedCells.size
    }
    fun containsCell(cellId: Int):Boolean {
        return playedCells.contains(cellId)
    }
    fun restartGame() {
        playedCells.clear()
    }
    // Returns 0 if row is full, cell number if only one cell is empty, -1 otherwise
    private fun checkRow(row: Int, rowSize:Int): Int {
        var emptyCell:Int = -1
        for (cellInRow in 1..rowSize) {
            val cell = (row - 1) * rowSize + cellInRow
            if (!playedCells.contains(cell)) {
                if (emptyCell == -1)
                    emptyCell = cell
                else
                    return -1
            }
        }
        if (emptyCell == -1)
            return 0
        return emptyCell
    }
    // Returns 0 if row is full, cell number if only one cell is empty, -1 otherwise
    private fun checkColumn(column: Int, columnSize:Int): Int {
        var emptyCell:Int = -1
        for (cellInColumn in 1..columnSize) { //1,4,7
            val cell = column + columnSize * (cellInColumn-1)
            if (!playedCells.contains(cell)) {
                if (emptyCell == -1)
                    emptyCell = cell
                else
                    return -1
            }
        }
        if (emptyCell == -1)
            return 0
        return emptyCell
    }
    // Returns 0 if row is full, cell number if only one cell is empty, -1 otherwise
    private fun checkDiagonal(diagonal:Int, diagonalSize:Int): Int {
        var emptyCell:Int = -1
        var step:Int
        if (diagonal == -1) step = diagonalSize else step = 1
        for (cellInRow in 1..diagonalSize) { //1,5,9 ; 3,5,7
            val cell = diagonalSize * (cellInRow-1) + step
            step += diagonal;
            if (!playedCells.contains(cell)) {
                if (emptyCell == -1)
                    emptyCell = cell
                else // more than 1 cell is empty
                    return -1
            }
        }
        if (emptyCell == -1)
            return 0
        return emptyCell
    }
    fun isWin(gameSize:Int): Boolean {
        val rowSize:Int = sqrt(gameSize.toFloat()).toInt()
        for (row in 1..rowSize)
            if (checkRow(row, rowSize) == 0)
                return true
        for (column in 1..rowSize)
            if (checkColumn(column, rowSize) == 0)
                return true
        for (diagonal in -1..1 step 2)
            if (checkDiagonal(diagonal, rowSize) == 0)
                return true
        return false
    }
    // Returns -1 if no win in next play, otherwise returns the cell number of the next play for win
    fun isWinInNextPlay(gameSize:Int, emptyCells:ArrayList<Int>) : Int {
        val rowSize:Int = sqrt(gameSize.toFloat()).toInt()
        for (row in 1..rowSize) {
            val cell = checkRow(row, rowSize);
            if (cell != -1 && emptyCells.contains(cell))
              return cell
        }
        for (column in 1..rowSize) {
            val cell = checkColumn(column, rowSize);
            if (cell != -1  && emptyCells.contains(cell))
                return cell
        }
        for (diagonal in -1..1 step 2) {
            val cell = checkDiagonal(diagonal, rowSize);
            if (cell != -1  && emptyCells.contains(cell))
                return cell
        }
        return -1
    }
}

class MainActivity : AppCompatActivity() {

    private val player1 = Player("You","X", Color.MAGENTA, PlayerType.HUMAN)
    private val player2 = Player("Android", "O", Color.CYAN, PlayerType.ANDROID)
    private var activePlayer = player1
    private var nextPlayer = player2
    private var gameButtons = ArrayList<Button>()
    private val gameSize = 9

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
                        gameButtons.add(childButton)
                }
        }
        print("gameButtons: ${gameButtons.size}")
        // TODO - create all buttons within the kotlin code (to allow size depended board)
        // https://stackoverflow.com/questions/15741520/adding-a-button-for-android-in-java-code
        // https://www.techotopia.com/index.php/Creating_an_Android_User_Interface_in_Java_Code_using_Android_Studio
    }

    protected fun buttonClick(view: View) { // TODO - refactor to extract cellID from String
        val buttonSelected = view as Button
        var cellID = buttonSelected.tag.toString().toInt()
 //       Toast.makeText(this, "ID:" + cellID, Toast.LENGTH_SHORT).show()
        playGame(cellID, buttonSelected)
    }
    private fun resetGame() {
        Thread.sleep(2000)
        player1.restartGame()
        player2.restartGame()
        activePlayer = player1
        nextPlayer = player2
        for (button in gameButtons) {
            button.text = ""
            button.setBackgroundColor(Color.GRAY)
            button.isEnabled = true
        }
    }
    private fun playGame(cellId: Int, buttonSelected: Button) {
        // play with the active player
        buttonSelected.text = activePlayer.symbol
        buttonSelected.setBackgroundColor(activePlayer.color)
        activePlayer.play(cellId)
        // update board
        buttonSelected.isEnabled = false   // Disable button press
        // check game over/winner
        // TODO ("move board logic to new class Board with statuses")
        // TODO ("move size of the board to be N")
        if (checkWinner() || (player1.numberOfPlays() + player2.numberOfPlays() == gameSize)) {
            resetGame()
        } else {
            // continue to next player
            updateNextPlayer()
            if (activePlayer.playerType == PlayerType.ANDROID) {
                autoPlay()
            }
        }
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
        var cellId = activePlayer.isWinInNextPlay(gameSize, emptyCells)
        if (cellId == -1) {
            // Find if there is critical cell for next player
            cellId = nextPlayer.isWinInNextPlay(gameSize, emptyCells)
            if (cellId == -1) {
                // random cell
                val r = Random()
                val randomIndex = r.nextInt(emptyCells.size - 0) + 0
                cellId = emptyCells[randomIndex]
            }
        }
        var buSelected:Button = button1
        for (button in gameButtons)
            if (button.tag.toString().toInt() == cellId) {
                buSelected = button
                break
            }
        playGame(cellId, buSelected)
    }
    private fun checkWinner(): Boolean {
        if (activePlayer.isWin(gameSize)) {
            Toast.makeText(this, activePlayer.name + " Won", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }

}