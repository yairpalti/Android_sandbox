package com.yairpalti.tictactoelocal

import java.util.ArrayList
import kotlin.math.sqrt

enum class PlayerType {
    HUMAN, ANDROID
}
class Player(var name:String, val symbol:String, val color:Int, var playerType: PlayerType = PlayerType.HUMAN) {
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
    data class DiagonalInfo(val startCell:Int, val direction:Int, val diagonalSize:Int)

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
        private fun checkCell(cell:Int, cellInRow:Int, maxRowSize:Int, checkWinOnly:Boolean=false): CellsOnLine {
            // maxRowSize can be different than rowSize - for some of the diagonals
            if (!playedCells.contains(cell)) {
                if (!emptyCells.contains(cell)) {
                    // Other player cell
                    countOther++
                    lastCellWasEmpty = false
                    if (!(cellInRow == 1 || cellInRow == maxRowSize) || countOther == 2) {
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
            else if (!checkWinOnly && countSingleEmptyOrMyInSequence == winSize)
                // in case checkWinOnly - ignore the NEXT_PLAY_WIN check (otherwise, could be a case of NEXT_PLAY_WIN before WIN- (for example game of win 3 of 4): _xxxx
                // check not at the end of the loop for the case the last empty cell won't win, but rather the middle one - example: X_XX_
                return CellsOnLine(CellsOnLineStatus.NEXT_PLAY_WIN, emptyCell)
            return CellsOnLine(CellsOnLineStatus.NOT_DETERMINE)
        }
        fun checkHorizontalRow(row: Int, checkWinOnly: Boolean=false): CellsOnLine {
            // TODO - break to two methods - one to check win and one to check next play win
            // Currently in case like this it will return "next play win" instead of win (for game of win 3 of 4): _xxxx
            // The same for the checkColumn, checkDiagonal
            reset()
            for (cellInRow in 1..rowSize) {
                val cell = (row - 1) * rowSize + cellInRow
                val cellsOnLine = checkCell(cell, cellInRow, rowSize, checkWinOnly)
                if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                    return cellsOnLine
            }
            return CellsOnLine(CellsOnLineStatus.NO_WIN)
        }
        fun checkColumn(column: Int, checkWinOnly: Boolean=false): CellsOnLine {
            reset()
            for (cellInColumn in 1..rowSize) { //1,4,7
                val cell = column + rowSize * (cellInColumn-1)
                val cellsOnLine = checkCell(cell, cellInColumn, rowSize, checkWinOnly)
                if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                    return cellsOnLine
            }
            return CellsOnLine(CellsOnLineStatus.NO_WIN)
        }

        fun checkDiagonal(diagonalInfo:DiagonalInfo, checkWinOnly: Boolean=false): CellsOnLine {
            reset()
            var step = rowSize + diagonalInfo.direction // direction = +-1
            var cell:Int = diagonalInfo.startCell
            for (cellInRow in 1..diagonalInfo.diagonalSize) {
                val cellsOnLine = checkCell(cell, cellInRow, diagonalInfo.diagonalSize, checkWinOnly)
                if (cellsOnLine.status != CellsOnLineStatus.NOT_DETERMINE)
                    return cellsOnLine
                cell += step
            }
            return CellsOnLine(CellsOnLineStatus.NO_WIN)
        }
    }
    private fun getDiagonalList(rowSize:Int, winSize:Int):ArrayList<DiagonalInfo> {
        var diagonalList = ArrayList<DiagonalInfo> ()
        diagonalList.add(DiagonalInfo(1,1,rowSize))
        diagonalList.add(DiagonalInfo(rowSize,-1,rowSize))
        if (winSize < rowSize) {
            diagonalList.add(DiagonalInfo(2,1,rowSize-1))
            diagonalList.add(DiagonalInfo(rowSize-1,-1,rowSize-1))
            diagonalList.add(DiagonalInfo(rowSize+1,1,rowSize-1))
            diagonalList.add(DiagonalInfo(2*rowSize,-1,rowSize-1))
        }
        return diagonalList
    }
    fun isWin(gameSize:Int, winSize:Int, emptyCells: ArrayList<Int>): Boolean {
        val gameStatus = checkBoard(gameSize, winSize, emptyCells, CellsOnLineStatus.WIN)
        if (gameStatus.status == CellsOnLineStatus.WIN)
            return true
        return false
    }

    fun isWinInNextPlay(gameSize:Int, winSize:Int, emptyCells: ArrayList<Int>) : CellsOnLine {
        return checkBoard(gameSize, winSize, emptyCells, CellsOnLineStatus.NEXT_PLAY_WIN)
    }

    private fun checkBoard(gameSize:Int, winSize:Int, emptyCells: ArrayList<Int>,lineStatusToCheck:CellsOnLineStatus) : CellsOnLine {
        val rowSize:Int = sqrt(gameSize.toFloat()).toInt()

        var rowStatus = RowStatus(rowSize, winSize, emptyCells)

        for (row in 1..rowSize) {
            val cellsOnLine = rowStatus.checkHorizontalRow(row);
            if (cellsOnLine.status == lineStatusToCheck)
                return cellsOnLine
        }
        for (column in 1..rowSize) {
            val cellsOnLine = rowStatus.checkColumn(column);
            if (cellsOnLine.status == lineStatusToCheck)
                return cellsOnLine
        }
        // diagonals
        var diagonalList = getDiagonalList(rowSize, winSize)
        for (i in 0 until diagonalList.size) {
            val cellsOnLine = rowStatus.checkDiagonal(diagonalList[i]);
            if (cellsOnLine.status == lineStatusToCheck)
                return cellsOnLine
        }
        return CellsOnLine(CellsOnLineStatus.NO_WIN)
    }
}