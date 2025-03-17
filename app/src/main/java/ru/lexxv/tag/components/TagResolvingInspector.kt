package ru.lexxv.tag.components

import ru.lexxv.tag.model.GameModel

class TagResolvingInspector {

    fun isResolvable(gameModel: GameModel): Boolean {
        val board = gameModel.getBoard().flatten().toMutableList()
        val emptyCellRow = findEmptyCellRow(gameModel)
        val inversions = countInversions(board)

        return checkResolvability(inversions, emptyCellRow)
    }

    // Находит строку пустой клетки (считая снизу, начиная с 1)
    private fun findEmptyCellRow(gameModel: GameModel): Int {
        val board = gameModel.getBoard()
        for (row in 0..3) {
            for (col in 0..3) {
                if (board[row][col] == 0) {
                    return 4 - row // Строки считаются снизу (1-4)
                }
            }
        }
        return -1 // Не должно происходить
    }

    // Считает количество инверсий (игнорируя 0)
    private fun countInversions(board: List<Int>): Int {
        var count = 0
        val filtered = board.filter { it != 0 }

        for (i in filtered.indices) {
            for (j in i + 1 until filtered.size) {
                if (filtered[i] > filtered[j]) count++
            }
        }
        return count
    }

    // Проверяет условие разрешимости
    private fun checkResolvability(inversions: Int, emptyRow: Int): Boolean {
        return when {
            emptyRow % 2 == 0 -> inversions % 2 == 1 // Чётная строка → нечётные инверсии
            else -> inversions % 2 == 0 // Нечётная строка → чётные инверсии
        }
    }
}
