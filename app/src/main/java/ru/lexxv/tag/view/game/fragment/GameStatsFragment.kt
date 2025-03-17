package ru.lexxv.tag.view.game.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.lexxv.tag.R
import ru.lexxv.tag.view.game.GameActivity
import ru.lexxv.tag.viewmodel.GameViewModel

class GameStatsFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var moveCounter: TextView
    private lateinit var timeCounter: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_game_stats, container, false)
    }

    private fun initViews(view: View) {
        viewModel = (activity as GameActivity).viewModel
        moveCounter = view.findViewById(R.id.moveCounter)
        timeCounter = view.findViewById(R.id.timeCounter)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.moves.observe(viewLifecycleOwner) { moves ->
            moveCounter.text = "Ходов: $moves"
        }
        viewModel.time.observe(viewLifecycleOwner) { time ->
            timeCounter.text = "Время: ${String.format("%.1f", time)}"
        }
    }
}
