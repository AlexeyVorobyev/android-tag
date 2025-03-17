package ru.lexxv.tag.view.game

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ru.lexxv.tag.R
import ru.lexxv.tag.viewmodel.GameViewModel
import androidx.lifecycle.ViewModelProvider
import ru.lexxv.tag.components.GameViewModelFactory
import ru.lexxv.tag.view.game.fragment.GameControlsFragment
import ru.lexxv.tag.view.game.fragment.GameGridFragment
import ru.lexxv.tag.view.game.fragment.GameStatsFragment
import androidx.fragment.app.commit

class GameActivity : AppCompatActivity() {
    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val factory = GameViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container_grid, GameGridFragment())
                replace(R.id.fragment_container_stats, GameStatsFragment())
                replace(R.id.fragment_container_controls, GameControlsFragment())
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
}
