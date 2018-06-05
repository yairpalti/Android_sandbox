 package com.yairpalti.tictactoelocal

import android.graphics.Color
import android.util.Log
import android.widget.Button
import java.util.*
import java.util.logging.Logger

 object Game {
     //   TODO: Change Game to be singleton (but context can't be static)
     // lateinit var context: Context
     var gameSize: Int = 0
         set(value) {
             field = value // backing field
             resetEmptyCells()
         }
     var winSize: Int = 0
     private val player1 = Player("You", "X", Color.MAGENTA, PlayerType.HUMAN)
     private val player2 = Player("Android", "O", Color.CYAN, PlayerType.ANDROID)
     private var activePlayer = player1
     private var nextPlayer = player2
     private var gameButtons = ArrayList<Button>()
     private var emptyCells = ArrayList<Int>()

     private fun resetEmptyCells() {
         emptyCells.clear()
         for (i in 1..gameSize)
             emptyCells.add(i)
     }
     fun resetButtons() {
         gameButtons.clear()
     }

     fun addButton(button: Button) {
         gameButtons.add(button)
     }

//     inline fun log(lambda: () -> String) {
//         if (BuildConfig.DEBUG) {
//             Log.d("TAG", lambda())
//         }
//     }

     fun playGame(cellId: Int, buttonSelected: Button): GameStatus {
//         log {"Game:playGame"}
         // play with the active player
         buttonSelected.text = activePlayer.symbol
         buttonSelected.setBackgroundColor(activePlayer.color)
         activePlayer.play(cellId)
         emptyCells.remove(cellId)
         // update board
         buttonSelected.isEnabled = false   // Disable button press
         // check game over/winner
         var gameStatus = getGameStatus()
         if (gameStatus.status != GameStatuses.GAME_ON)
//            gameOver(gameStatus.endGameMessage)
         else {
             // continue to next player
             updateNextPlayer()
             if (activePlayer.playerType == PlayerType.ANDROID) {
                 gameStatus = autoPlay()
             }
         }
         return gameStatus
     }

     fun enableButtons(on: Boolean, alsoReset: Boolean = false) {
         for (button in gameButtons) {
             button.isEnabled = on
             if (alsoReset) {
                 button.setBackgroundColor(Color.GRAY)
                 button.text = ""
             }
         }
     }

     fun reset() {
         player1.restartGame()
         player2.restartGame()
         resetEmptyCells()
         activePlayer = player1
         nextPlayer = player2
         enableButtons(true, true)
     }

     private fun updateNextPlayer() {
         val currPlayer = activePlayer
         activePlayer = nextPlayer
         nextPlayer = currPlayer
     }

     private fun autoPlay(): GameStatus {
         // TODO("unit test that we don't reach this method with no emptyCells")
         // TODO("add logic to find best cell to play")
         // Find if there is a cell that will enable winning
         var cellsOnLine = activePlayer.isWinInNextPlay(gameSize, winSize, emptyCells)
         var cellId = cellsOnLine.cellId
         if (cellsOnLine.status == Player.CellsOnLineStatus.NO_WIN) {
             // Find if there is critical cell for next player
             cellsOnLine = nextPlayer.isWinInNextPlay(gameSize, winSize, emptyCells)
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
         return playGame(cellId, buSelected)
     }

     enum class GameStatuses {
         WIN, NO_WIN, GAME_ON
     }

     data class GameStatus(var status: GameStatuses, var endGameMessage: String)

     private fun getGameStatus(): GameStatus {
         if (activePlayer.isWin(gameSize, winSize, emptyCells))
             return GameStatus(GameStatuses.WIN, activePlayer.name + " Won")
         else if ((player1.numberOfPlays() + player2.numberOfPlays() == gameSize))
             return GameStatus(GameStatuses.NO_WIN, "No Winner")
         // TODO - move strings to resources (currently crash)
         //             return GameStatus(GameStatuses.NO_WIN, Resources.getSystem().getString(R.string.NO_WINNER))
         return GameStatus(GameStatuses.GAME_ON, "")
     }
 }