package ru.lexxv.tag.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.lexxv.tag.model.GameModel
import android.os.Handler
import android.os.Looper

class GameViewModel : ViewModel() {
    private val model = GameModel()

    // LiveData для доски и количества ходов
    private val _board = MutableLiveData<List<Int>>()
    val board: LiveData<List<Int>> = _board

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> = _moves

    private val _time = MutableLiveData<Float>()
    val time: LiveData<Float> = _time

    private var _isRunning = MutableLiveData<Boolean>()
    val isRunning: LiveData<Boolean> = _isRunning

    private val handler = Handler(Looper.getMainLooper())
    private var elapsedTime = 0.0f


    init {
        _board.value = model.getBoard()
        _moves.value = model.getMoves()
        _time.value = 0.0f
        _isRunning.value = model.getIsRunning()
    }

    private val timeRunnable = object : Runnable {
        override fun run() {
            if (_isRunning.value!!) {
                elapsedTime += 0.1f
                _time.value = elapsedTime
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
        if (_isRunning.value!!) {
            stopTimer()
        }
        else {
            startTimer()
        }
    }

    private fun updateState() {
        _board.value = model.getBoard()
        _moves.value = model.getMoves()
        _isRunning.value = model.getIsRunning()
    }

    fun resetGame() {
        model.resetGame()
        stopTimer()
        elapsedTime = 0.0f
        _time.value = elapsedTime
        startTimer()
        updateState()
    }

    fun makeMove(position: Int) {
        model.makeMove(position)
        updateState()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(timeRunnable)
    }
}