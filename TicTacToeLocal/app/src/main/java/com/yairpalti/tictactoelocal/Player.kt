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
    enum class CellsOnLineStatus {
        WIN, NO_WIN, NEXT_PLAY_WIN
    }
    data class CellsOnLine(var status:CellsOnLineStatus, val cellId:Int=0)

    private fun checkRow(row: Int, rowSize:Int): CellsOnLine {
        var emptyCell:Int = -1
        for (cellInRow in 1..rowSize) {
            val cell = (row - 1) * rowSize + cellInRow
            if (!playedCells.contains(cell)) {
                if (emptyCell == -1)
                    emptyCell = cell
                else
                    return CellsOnLine(CellsOnLineStatus.NO_WIN)
            }
        }
        if (emptyCell == -1)
            return CellsOnLine(CellsOnLineStatus.WIN)
        return CellsOnLine(CellsOnLineStatus.NEXT_PLAY_WIN, emptyCell)
    }

    private fun checkColumn(column: Int, columnSize:Int): CellsOnLine {
        var emptyCell:Int = -1
        for (cellInColumn in 1..columnSize) { //1,4,7
            val cell = column + columnSize * (cellInColumn-1)
            if (!playedCells.contains(cell)) {
                if (emptyCell == -1)
                    emptyCell = cell
                else
                    return CellsOnLine(CellsOnLineStatus.NO_WIN)
            }
        }
        if (emptyCell == -1)
            return CellsOnLine(CellsOnLineStatus.WIN)
        return CellsOnLine(CellsOnLineStatus.NEXT_PLAY_WIN, emptyCell)
    }
    private fun checkDiagonal(diagonal:Int, diagonalSize:Int): CellsOnLine {
        var emptyCell:Int = -1
        var step:Int
        if (diagonal == -1) step = diagonalSize else step = 1
        for (cellInRow in 1..diagonalSize) {
            val cell = diagonalSize * (cellInRow-1) + step
            step += diagonal;
            if (!playedCells.contains(cell)) {
                if (emptyCell == -1)
                    emptyCell = cell
                else // more than 1 cell is empty
                    return CellsOnLine(CellsOnLineStatus.NO_WIN)
            }
        }
        if (emptyCell == -1)
            return CellsOnLine(CellsOnLineStatus.WIN)
        return CellsOnLine(CellsOnLineStatus.NEXT_PLAY_WIN, emptyCell)
    }
    // TODO - add logic to allow another game version to win with N-1 (In this case need to check also that there is continues order and there is no other player blocking - probably need to pass emptyCells )
    fun isWin(gameSize:Int): Boolean {
        val rowSize:Int = sqrt(gameSize.toFloat()).toInt()
        for (row in 1..rowSize)
            if (checkRow(row, rowSize).status == CellsOnLineStatus.WIN)
                return true
        for (column in 1..rowSize)
            if (checkColumn(column, rowSize).status == CellsOnLineStatus.WIN)
                return true
        for (diagonal in -1..1 step 2)
            if (checkDiagonal(diagonal, rowSize).status == CellsOnLineStatus.WIN)
                return true
        return false
    }
    // checks if the empty is cell is really empty
    private fun isWinInNextPlayForLine(cellsOnLine:CellsOnLine, emptyCells: ArrayList<Int>): CellsOnLine {
        if (cellsOnLine.status == CellsOnLineStatus.NEXT_PLAY_WIN && !emptyCells.contains(cellsOnLine.cellId))
            cellsOnLine.status = CellsOnLineStatus.NO_WIN
        return cellsOnLine
    }
    // Returns -1 if no win in next play, otherwise returns the cell number of the next play for win
    fun isWinInNextPlay(gameSize:Int, emptyCells: ArrayList<Int>) : CellsOnLine {
        val rowSize:Int = sqrt(gameSize.toFloat()).toInt()
        for (row in 1..rowSize) {
            var cellsOnLine = checkRow(row, rowSize);
            cellsOnLine = isWinInNextPlayForLine(cellsOnLine, emptyCells)
            if (cellsOnLine.status == CellsOnLineStatus.NEXT_PLAY_WIN)
                return  cellsOnLine

        }
        for (column in 1..rowSize) {
            var cellsOnLine = checkColumn(column, rowSize);
            cellsOnLine = isWinInNextPlayForLine(cellsOnLine, emptyCells)
            if (cellsOnLine.status == CellsOnLineStatus.NEXT_PLAY_WIN)
                return  cellsOnLine
        }
        for (diagonal in -1..1 step 2) {
            var cellsOnLine = checkDiagonal(diagonal, rowSize);
            cellsOnLine = isWinInNextPlayForLine(cellsOnLine, emptyCells)
            if (cellsOnLine.status == CellsOnLineStatus.NEXT_PLAY_WIN)
                return  cellsOnLine
        }
        return CellsOnLine(CellsOnLineStatus.NO_WIN)
    }
}