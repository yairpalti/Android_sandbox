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
        WIN, NO_WIN, NEXT_PLAY_WIN, NOT_DETERMINE
    }
    data class CellsOnLine(var status:CellsOnLineStatus, val cellId:Int=0)

    inner class RowStatus(private val rowSize:Int, private val winSize:Int, private val emptyCells: ArrayList<Int>) {
        private var emptyCell:Int = -1
        private var countSingleEmptyOrMyInSequence:Int = 0
        private var countOther:Int = -1
        private var countMyInSequence:Int = 0
        private var lastCellWasMine = false
        private var lastCellWasEmpty = false

        private fun reset() {
            emptyCell = -1
            countSingleEmptyOrMyInSequence = 0
            countOther = -1
            countMyInSequence = 0
            lastCellWasMine = false
            lastCellWasEmpty = false
        }
        private fun checkCell(cell:Int, cellInRow:Int): CellsOnLine {
            if (!playedCells.contains(cell)) {
                if (!emptyCells.contains(cell)) {
                    // Other player cell
                    countOther++
                    lastCellWasEmpty = false
                    if (!(cellInRow == 1 || cellInRow == rowSize) || countOther == 2) {
                        // other player in the middle or at the two ends
                        return CellsOnLine(CellsOnLineStatus.NO_WIN)
                    }
                } else {
                    // empty cell
                    if (lastCellWasEmpty)
                        countSingleEmptyOrMyInSequence = 0
                    else if (emptyCell != -1)
                        countSingleEmptyOrMyInSequence--
                    countSingleEmptyOrMyInSequence++
                    lastCellWasEmpty = true
                    emptyCell = cell
           }
                lastCellWasMine = false
            } else {
                // Player cell
                if (!lastCellWasMine)
                    countMyInSequence = 0
                countMyInSequence++
                countSingleEmptyOrMyInSequence++
                lastCellWasMine = true
                lastCellWasEmpty = false
            }
            // Check status
            if (countMyInSequence == winSize)
                return CellsOnLine(CellsOnLineStatus.WIN)
            else if (countSingleEmptyOrMyInSequence == winSize)
                // check here (and not at the end) for the case the last empty cell won't win - example: X_XX_
                return CellsOnLine(CellsOnLineStatus.NEXT_PLAY_WIN, emptyCell)
            return CellsOnLine(CellsOnLineStatus.NOT_DETERMINE)
        }
        fun checkHorizontalRow(row: Int): CellsOnLine {
            // TODO - break to two methods - one to check win and one to check next play win
            // Currently in case like this it will return "next play win" instead of win (for game of win 3 of 4): _xxxx
            // The same for the checkColumn, checkDiagonal
            reset()
            for (cellInRow in 1..rowSize) {
                val cell = (row - 1) * rowSize + cellInRow
                val cellsOnLine = checkCell(cell, cellInRow)
                if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                    return cellsOnLine
            }
            return CellsOnLine(CellsOnLineStatus.NO_WIN)
        }
        fun checkColumn(column: Int): CellsOnLine {
            reset()
            for (cellInColumn in 1..rowSize) { //1,4,7
                val cell = column + rowSize * (cellInColumn-1)
                val cellsOnLine = checkCell(cell, cellInColumn)
                if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                    return cellsOnLine
            }
            return CellsOnLine(CellsOnLineStatus.NO_WIN)
        }
        fun checkDiagonal(diagonal:Int): CellsOnLine {
            // TODO need to check also other diagonals in case winSize is less than game size
            reset()
            var step:Int
            if (diagonal == -1) step = rowSize else step = 1
            for (cellInRow in 1..rowSize) {
                val cell = rowSize * (cellInRow-1) + step
                step += diagonal;
                val cellsOnLine = checkCell(cell, cellInRow)
                if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                    return cellsOnLine
            }
            return CellsOnLine(CellsOnLineStatus.NO_WIN)
        }
    }
 /*   private fun checkHorizontalRow(row: Int, rowSize:Int, winSize:Int, emptyCells: ArrayList<Int>): CellsOnLine {
        // TODO - move this method to be inside the RowStatus class
        // TODO - break to two methods - one to check win and one to check next play win
        // Currently in case like this it will return "next play win" instead of win (for game of win 3 of 4): _xxxx
        // The same for the checkColumn, checkDiagonal
        var rowStatus = RowStatus(winSize,emptyCells)

        for (cellInRow in 1..rowSize) {
            val cell = (row - 1) * rowSize + cellInRow
            val cellsOnLine = rowStatus.checkCell(cell, cellInRow, rowSize)
            if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                return cellsOnLine
        }
        return CellsOnLine(CellsOnLineStatus.NO_WIN)
    }*/

 /*   private fun checkColumn(column: Int, columnSize:Int, winSize:Int, emptyCells: ArrayList<Int>): CellsOnLine {
        var rowStatus = RowStatus(winSize,emptyCells)

        for (cellInColumn in 1..columnSize) { //1,4,7
            val cell = column + columnSize * (cellInColumn-1)
            val cellsOnLine = rowStatus.checkCell(cell, cellInColumn, columnSize)
            if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                return cellsOnLine
        }
        return CellsOnLine(CellsOnLineStatus.NO_WIN)
    }*/

 /*   private fun checkDiagonal(diagonal:Int, diagonalSize:Int, winSize:Int, emptyCells: ArrayList<Int>): CellsOnLine {
        // TODO need to check also other diagonals in case winSize is less than game size
        var rowStatus = RowStatus(winSize,emptyCells)
        var step:Int
        if (diagonal == -1) step = diagonalSize else step = 1
        for (cellInRow in 1..diagonalSize) {
            val cell = diagonalSize * (cellInRow-1) + step
            step += diagonal;
            val cellsOnLine = rowStatus.checkCell(cell, cellInRow, diagonalSize)
            if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                return cellsOnLine
        }
        return CellsOnLine(CellsOnLineStatus.NO_WIN)
    }*/

    fun isWin(gameSize:Int, winSize:Int, emptyCells: ArrayList<Int>): Boolean {
        val rowSize:Int = sqrt(gameSize.toFloat()).toInt()

        var rowStatus = RowStatus(rowSize, winSize, emptyCells)

        for (row in 1..rowSize)
            if (rowStatus.checkHorizontalRow(row).status == CellsOnLineStatus.WIN)
                return true
        for (column in 1..rowSize)
            if (rowStatus.checkColumn(column).status == CellsOnLineStatus.WIN)
                return true
        // diagonals
//        var diagonalStartCells = ArrayList<Int> ()
//        diagonalStartCells.add(1)

        for (diagonal in -1..1 step 2)
            if (rowStatus.checkDiagonal(diagonal).status == CellsOnLineStatus.WIN)
                return true
        return false
    }

    // Returns -1 if no win in next play, otherwise returns the cell number of the next play for win
    fun isWinInNextPlay(gameSize:Int, winSize:Int, emptyCells: ArrayList<Int>) : CellsOnLine {
        val rowSize:Int = sqrt(gameSize.toFloat()).toInt()

        var rowStatus = RowStatus(rowSize, winSize, emptyCells)

        for (row in 1..rowSize) {
            val cellsOnLine = rowStatus.checkHorizontalRow(row);
            if (cellsOnLine.status == CellsOnLineStatus.NEXT_PLAY_WIN)
                return cellsOnLine
        }
        for (column in 1..rowSize) {
            val cellsOnLine = rowStatus.checkColumn(column);
            if (cellsOnLine.status == CellsOnLineStatus.NEXT_PLAY_WIN)
                return cellsOnLine
        }
        for (diagonal in -1..1 step 2) {
            val cellsOnLine = rowStatus.checkDiagonal(diagonal);
            if (cellsOnLine.status == CellsOnLineStatus.NEXT_PLAY_WIN)
                return cellsOnLine
        }
        return CellsOnLine(CellsOnLineStatus.NO_WIN)
    }
}