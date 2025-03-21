package ru.lexxv.tag.model

import ru.lexxv.tag.components.TagResolvingInspector


class GameModel {
    private var board: Array<Array<Int>> = initialBoard()
    private var moves: Int = 0
    private var isRunning: Boolean = false
    private var isWon: Boolean = false
    private var time: Float = 0.0F

    private val resolvingInspector: TagResolvingInspector = TagResolvingInspector()

    private fun initialBoard(): Array<Array<Int>> {
        val numbers = (1..14).toMutableList() // Перемешиваем числа от 0 до 15
        numbers.add(0)
        numbers.add(15)
//        val numbers = (0..15).shuffled()  // Перемешиваем числа от 0 до 15
        return Array(4) { row ->
            Array(4) { col -> numbers[row * 4 + col] }
        }
    }

    fun resetGame() {
        board = initialBoard()
        time = 0.0F
        moves = 0
        isRunning = false
        isWon = false

        val isResolvable = resolvingInspector.isResolvable(this)

        if (!isResolvable) {
            resetGame()
        }
    }

    fun pause() {
        isRunning = false
    }

    fun continueGame() {
        if (isWon) {
            return
        }

        isRunning = true
    }

    fun incrementTime(input: Float) {
        time += input
    }

    fun makeMove(row: Int, col: Int) {
        if (!isRunning) {
            return
        }

        if (row !in 0..3 || col !in 0..3 || board[row][col] == 0) return

        // Проверяем все четыре направления
        val directions = listOf(
            -1 to 0, // Вверх
            1 to 0,  // Вниз
            0 to -1,  // Влево
            0 to 1    // Вправо
        )

        for ((dx, dy) in directions) {
            val newRow = row + dx
            val newCol = col + dy

            if (newRow in 0..3 && newCol in 0..3 && board[newRow][newCol] == 0) {
                // Меняем местами с пустой ячейкой
                board[newRow][newCol] = board[row][col]
                board[row][col] = 0
                moves++

                updateIsWon()

                return
            }
        }
    }

    private fun updateIsWon() {
        var expectedNumber = 1
        for (row in 0..3) {
            for (col in 0..3) {
                val value = board[row][col]

                // Для последней ячейки проверяем 0
                if (row == 3 && col == 3) {
                    if (value != 0) return
                }
                // Для остальных — последовательность 1-15
                else {
                    if (value != expectedNumber) return
                    expectedNumber++
                }
            }
        }

        isWon = true
        isRunning = false
    }

    fun getBoard(): Array<Array<Int>> = board
    fun getMoves(): Int = moves
    fun getIsRunning(): Boolean = isRunning
    fun getIsWon(): Boolean = isWon
    fun getTime(): Float = time
}
