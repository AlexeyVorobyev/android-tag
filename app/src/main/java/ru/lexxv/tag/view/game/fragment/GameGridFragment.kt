package ru.lexxv.tag.view.game.fragment
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.lexxv.tag.R
import ru.lexxv.tag.view.game.GameActivity
import ru.lexxv.tag.viewmodel.GameViewModel

class GameGridFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var gridLayout: GridLayout
    private val spacing = 8

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_game_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as GameActivity).viewModel
        gridLayout = view.findViewById(R.id.gridLayout)

        setupObservers()
    }

    private fun layoutListenerFactory() =
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                gridLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Теперь gridLayout.width не равен 0
                updateGameGrid(viewModel.board.value ?: emptyList())
            }
        }

    private fun setupObservers() {
        viewModel.board.observe(viewLifecycleOwner) { board ->
            updateGameGrid(board)
        }
        gridLayout.viewTreeObserver.addOnGlobalLayoutListener(
            layoutListenerFactory()
        )
    }

    private fun getCellSize(): Int =
        (
                gridLayout.width -
                        gridLayout.paddingLeft -
                        gridLayout.paddingRight -
                        spacing * 8
                ) / 4

    private fun updateGameGrid(board: List<Int>) {
        if (gridLayout.width == 0) return

        gridLayout.removeAllViews()
        gridLayout.columnCount = 4

        val cellSize = getCellSize()

        board.forEachIndexed { index, number ->
            gridLayout.addView(cellViewFactory(number, cellSize, index))
        }
    }

    private fun cellViewFactory(number: Int, size: Int, index: Int): TextView {
        val view = TextView(requireContext()).apply {
            text = if (number > 0) number.toString() else ""
            textSize = 24f
            gravity = Gravity.CENTER
            background = if (number > 0) ContextCompat.getDrawable(context, R.drawable.cell_background) else null
            layoutParams = GridLayout.LayoutParams().apply {
                width = size
                height = size
                setMargins(spacing, spacing, spacing, spacing)
            }
        }

        val row = index / 4
        val col = index % 4

        view.setOnClickListener {
            val newPosition = viewModel.makeMove(row, col)
            if (newPosition != null) {
                animateTileMovement(view, row, col, newPosition.first, newPosition.second, size)
            }
        }

        return view
    }

    private fun animateTileMovement(view: View, oldRow: Int, oldCol: Int, newRow: Int, newCol: Int, size: Int) {
        val deltaX = (newCol - oldCol) * size
        val deltaY = (newRow - oldRow) * size

        val duration = 200L

        ObjectAnimator.ofFloat(view, "translationX", 0f, deltaX.toFloat()).apply {
            this.duration = duration
            start()
        }

        ObjectAnimator.ofFloat(view, "translationY", 0f, deltaY.toFloat()).apply {
            this.duration = duration
            start()
        }

        view.postDelayed({
            viewModel.updateState()
        }, duration)
    }
}