package ru.lexxv.tag.view

import android.os.Bundle
import android.view.Gravity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        gridLayout = findViewById(R.id.gridLayout)

        setupObservers()
        findViewById<Button>(R.id.newGameButton).setOnClickListener {
            viewModel.resetGame()
        }
    }

    private fun setupObservers() {
        viewModel.board.observe(this) { board ->
            updateGameGrid(board)
        }
        viewModel.moves.observe(this) { moves ->
            // Здесь можно обновить UI, показывающий количество ходов, если нужно.
        }
    }

    private fun getCellSize(): Int =
        (gridLayout.width - gridLayout.paddingLeft - gridLayout.paddingRight)

    private fun updateGameGrid(board: List<Int>) {
        gridLayout.removeAllViews()
        gridLayout.columnCount = 4  // Устанавливаем 4 колонки

        board.forEach { number ->
            val cell = createCellView(number)
            gridLayout.addView(cell)
        }
    }

    private fun createCellView(number: Int): TextView {
        val size = getCellSize()
        return TextView(this).apply {
            text = if (number > 0) number.toString() else ""
            textSize = 24f
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(context, R.drawable.cell_background)
            layoutParams = GridLayout.LayoutParams().apply {
                width = size
                height = size
                rightMargin = 5
                bottomMargin = 5
            }
        }
    }
}