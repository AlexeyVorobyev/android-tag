package ru.lexxv.tag.view.game.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.lexxv.tag.R
import ru.lexxv.tag.view.StatisticsActivity
import ru.lexxv.tag.view.game.GameActivity
import ru.lexxv.tag.viewmodel.GameViewModel

class GameControlsFragment : Fragment() {
    private lateinit var viewModel: GameViewModel
    private lateinit var newGameButton: Button
    private lateinit var pauseButton: Button
    private lateinit var statisticsButton: Button

    private fun initViews(view: View) {
        viewModel = (activity as GameActivity).viewModel
        newGameButton = view.findViewById(R.id.newGameButton)
        pauseButton = view.findViewById(R.id.pauseButton)
        statisticsButton = view.findViewById(R.id.statisticsButton)
    }

    private fun setupListeners() {
        newGameButton.setOnClickListener { viewModel.resetGame() }
        pauseButton.setOnClickListener { viewModel.pause() }
        statisticsButton.setOnClickListener {
            startActivity(Intent(requireContext(), StatisticsActivity::class.java))
        }
    }

    private fun setupObservers() {
        viewModel.isRunning.observe(viewLifecycleOwner) { state ->
            pauseButton.text = if (state) {
                getString(R.string.pause_game)
            } else {
                getString(R.string.continue_game)
            }
        }
        viewModel.isWon.observe(viewLifecycleOwner) { state ->
            pauseButton.isEnabled = !state
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_game_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupListeners()
        setupObservers()
    }
}
