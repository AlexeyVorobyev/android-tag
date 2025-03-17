package ru.lexxv.tag.view

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ru.lexxv.tag.R
import ru.lexxv.tag.components.GameViewModelFactory
import ru.lexxv.tag.viewmodel.GameViewModel

class GameActivity : AppCompatActivity() {
    private lateinit var viewModel: GameViewModel
    private lateinit var gridLayout: GridLayout
    private lateinit var moveCounter: TextView
    private lateinit var timeCounter: TextView
    private lateinit var pauseButton: Button
    private lateinit var statisticsButton: Button
    private lateinit var newGameButton: Button

    private val spacing = 8

    private fun setupViews() {
        val factory = GameViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)
        gridLayout = findViewById(R.id.gridLayout)
        moveCounter = findViewById(R.id.moveCounter)
        timeCounter = findViewById(R.id.timeCounter)
        pauseButton = findViewById(R.id.pauseButton)
        statisticsButton = findViewById(R.id.statisticsButton)
        newGameButton = findViewById(R.id.newGameButton)
    }

    private fun setupListeners() {
        newGameButton.setOnClickListener {
            viewModel.resetGame()
        }
        pauseButton.setOnClickListener {
            viewModel.pause()
        }
        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
        // Дожидаемся полной отрисовки макета
        gridLayout.viewTreeObserver.addOnGlobalLayoutListener(
            layoutListenerFactory()
        )
    }

    private fun layoutListenerFactory() =
        object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            gridLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

            // Теперь gridLayout.width не равен 0
            updateGameGrid(viewModel.board.value ?: emptyList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        setupViews()
        setupObservers()
        setupListeners()
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
        viewModel.isWon.observe(this) { isWon ->
            if (isWon) {
                // Показать сообщение о победе, например:
                AlertDialog.Builder(this)
                    .setTitle("Победа!")
                    .setMessage("Вы собрали головоломку за ${viewModel.moves.value} ходов")
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
            }
        }
    }

    private fun getCellSize(): Int =
        (
            gridLayout.width -
            gridLayout.paddingLeft -
            gridLayout.paddingRight -
            spacing * 8
        ) / 4

    private fun updateGameGrid(board: List<Int>) {
        if (gridLayout.width == 0) return  // Предотвращаем вызов до полной отрисовки

        gridLayout.removeAllViews()
        gridLayout.columnCount = 4  // Устанавливаем 4 колонки

        val cellSize = getCellSize()

        board.forEachIndexed { index, number ->
            gridLayout.addView(
                cellViewFactory(number, cellSize, index) // Передаем индекс
            )
        }
    }

    private fun cellViewFactory(number: Int, size: Int, index: Int): TextView {
        val view = TextView(this).apply {
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

        val row = index / 4
        val col = index % 4
        view.setOnClickListener {
            viewModel.makeMove(row, col)
        }

        return view
    }
}
