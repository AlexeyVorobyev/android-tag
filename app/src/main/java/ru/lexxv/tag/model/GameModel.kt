package ru.lexxv.tag.model


class GameModel {
    private var board: List<Int> = initialBoard()
    private var moves: Int = 0
    private var isRunning: Boolean = false

    private fun initialBoard(): List<Int> = List(16) { it }.shuffled()

    fun resetGame() {
        board = initialBoard()
        moves = 0
        isRunning = false
    }

    fun pause() {
        isRunning = false
    }

    fun continueGame() {
        isRunning = true
    }

    fun makeMove(position: Int) {
        throw NotImplementedError()
    }

    fun getBoard(): List<Int> = board
    fun getMoves(): Int = moves
    fun getIsRunning(): Boolean = isRunning
}