package ru.lexxv.tag.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.lexxv.tag.model.GameModel
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.lexxv.tag.database.repository.GameRepository

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GameRepository = GameRepository(application)

    private val model: GameModel = GameModel()

    private val _board = MutableLiveData<Array<Array<Int>>>()
    val board: LiveData<List<Int>> = _board.map {
        it.flatten()
    }

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> = _moves

    private val _time = MutableLiveData<Float>()
    val time: LiveData<Float> = _time

    private val _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    private val _isWon = MutableLiveData<Boolean>()
    val isWon: LiveData<Boolean> = _isWon

    private val handler = Handler(Looper.getMainLooper())

    private var isGameSaved = false

    init {
        updateState()
    }

    private fun saveGameResult() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveGameResult(model.getMoves(), model.getTime())
        }
    }

    private val timeRunnable = object : Runnable {
        override fun run() {
            if (_isRunning.value!!) {
                model.incrementTime(0.1f)
                _time.value = model.getTime()
                handler.postDelayed(this, 100) // Обновление каждые 100 мс (0.1 сек)
            }
        }
    }

    private fun startTimer() {
        model.continueGame()
        handler.post(timeRunnable)
        updateState()
    }

    private fun stopTimer() {
        model.pause()
        handler.removeCallbacks(timeRunnable)
        updateState()
    }

    fun pause() {
        if (_isRunning.value == true) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    fun updateState() {
        _board.value = model.getBoard()
        _moves.value = model.getMoves()
        _time.value = model.getTime()
        _isRunning.value = model.getIsRunning()
        _isWon.value = model.getIsWon()

        if (model.getIsWon() && !isGameSaved) {
            saveGameResult()
            isGameSaved = true
        }
    }

    fun resetGame() {
        model.resetGame()
        stopTimer()
        startTimer()
        updateState()
        isGameSaved = false
    }

    fun makeMove(row: Int, col: Int): Pair<Int, Int>? {
        val oldBoard = model.getBoard().map { it.clone() } // Сохраняем старое состояние
        model.makeMove(row, col) // Пытаемся сделать ход

        val newBoard = model.getBoard()

        // Проверяем, куда передвинулась плитка
        for (newRow in 0..3) {
            for (newCol in 0..3) {
                if (oldBoard[row][col] == newBoard[newRow][newCol] && (newRow != row || newCol != col)) {
                    return Pair(newRow, newCol) // Возвращаем новую позицию плитки
                }
            }
        }

        return null // Если плитка не сдвинулась, возвращаем null
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(timeRunnable)
    }
}
