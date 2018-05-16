package com.yairpalti.tictactoelocal

import java.util.ArrayList
import kotlin.math.sqrt

enum class PlayerType {
    HUMAN, ANDROID
}
class Player(val name:String, val symbol:String, val color:Int, val playerType: PlayerType = PlayerType.HUMAN) {
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
    // TODO - add logic to allow another game version to win with N-1
    // (In this case need to check also that there is continues order)
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
    fun isWinInNextPlay(gameSize:Int, emptyCells: ArrayList<Int>) : Int {
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