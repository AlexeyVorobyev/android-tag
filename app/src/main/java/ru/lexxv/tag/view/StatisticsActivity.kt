package ru.lexxv.tag.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.lexxv.tag.R
import ru.lexxv.tag.database.repository.GameRepository

class StatisticsActivity : ComponentActivity() {
    private lateinit var statsTextView: TextView
    private lateinit var backButton: Button
    private lateinit var repository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        statsTextView = findViewById(R.id.statsTextView)
        backButton = findViewById(R.id.backButton)

        repository = GameRepository(this)

        loadStatistics()

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadStatistics() {
        lifecycleScope.launch(Dispatchers.IO) {
            val results = repository.getAllGameResults()
            val formattedResults = results.joinToString("\n") { "Ходы: ${it.moves}, Время: ${it.time}" }

            withContext(Dispatchers.Main) {
                statsTextView.text = formattedResults.ifEmpty { "Нет данных" }
            }
        }
    }
}