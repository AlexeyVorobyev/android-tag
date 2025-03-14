package ru.lexxv.tag.model


class GameModel {
    private var board: List<Int> = initialBoard()
    private var moves: Int = 0

    private fun initialBoard(): List<Int> = List(16) { it }.shuffled()

    fun resetGame() {
        board = initialBoard()
        moves = 0
    }

    fun makeMove(position: Int) {
        throw NotImplementedError()
    }

    fun getBoard(): List<Int> = board
    fun getMoves(): Int = moves
}