package ru.lexxv.tag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.lexxv.tag.model.GameModel

class GameViewModel : ViewModel() {
    private val model = GameModel()

    // LiveData для доски и количества ходов
    private val _board = MutableLiveData<List<Int>>()
    val board: LiveData<List<Int>> = _board

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> = _moves

    init {
        // Инициализация LiveData значениями из модели
        _board.value = model.getBoard()
        _moves.value = model.getMoves()
    }

    fun resetGame() {
        model.resetGame()
        // Обновляем LiveData после сброса игры
        _board.value = model.getBoard()
        _moves.value = model.getMoves()
    }

    fun makeMove(position: Int) {
        model.makeMove(position)
        // Обновляем LiveData после каждого хода
        _board.value = model.getBoard()
        _moves.value = model.getMoves()
    }
}