package ru.lexxv.tag.view

import android.os.Bundle
import android.view.Gravity
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ru.lexxv.tag.R
import ru.lexxv.tag.viewmodel.GameViewModel

class GameActivity : AppCompatActivity() {
    private lateinit var viewModel: GameViewModel
    private lateinit var gridLayout: GridLayout
    private lateinit var moveCounter: TextView
    private lateinit var timeCounter: TextView
    private lateinit var pauseButton: Button

    private val spacing = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        gridLayout = findViewById(R.id.gridLayout)
        moveCounter = findViewById(R.id.moveCounter)
        timeCounter = findViewById(R.id.timeCounter)
        pauseButton = findViewById(R.id.pauseButton)

        setupObservers()
        findViewById<Button>(R.id.newGameButton).setOnClickListener {
            viewModel.resetGame()
        }
        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            viewModel.pause()
        }

        // Дожидаемся полной отрисовки макета
        gridLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                gridLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Теперь gridLayout.width не равен 0
                updateGameGrid(viewModel.board.value ?: emptyList())
            }
        })
    }

    private fun setupObservers() {
        viewModel.board.observe(this) { board ->
            updateGameGrid(board)
        }
        viewModel.moves.observe(this) { moves ->
            // Здесь можно обновить UI, показывающий количество ходов, если нужно.
        }
        viewModel.moves.observe(this) { moves ->
            moveCounter.text = "Ходов: $moves"
        }
        viewModel.time.observe(this) { time ->
            timeCounter.text = "Время: ${String.format("%.1f", time)}"
        }
        viewModel.isRunning.observe(this) { state ->
            if (state) {
                pauseButton.text = getString(R.string.pause_game)
            }
            else {
                pauseButton.text = getString(R.string.continue_game)
            }
        }
    }

    private fun getCellSize(): Int =
        (
            gridLayout.width -
            gridLayout.paddingLeft -
            gridLayout.paddingRight -
            spacing * 5
        ) / 4

    private fun updateGameGrid(board: List<Int>) {
        if (gridLayout.width == 0) return  // Предотвращаем вызов до полной отрисовки

        gridLayout.removeAllViews()
        gridLayout.columnCount = 4  // Устанавливаем 4 колонки

        val cellSize = getCellSize()

        board.forEach { number ->
            val cell = createCellView(number, cellSize)
            gridLayout.addView(cell)
        }
    }

    private fun createCellView(number: Int, size: Int): TextView =
        TextView(this).apply {
            text = if (number > 0) number.toString() else ""
            textSize = 24f
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(context, R.drawable.cell_background)
            layoutParams = GridLayout.LayoutParams().apply {
                width = size
                height = size
                setMargins(spacing,spacing,spacing,spacing)
            }
        }
}
