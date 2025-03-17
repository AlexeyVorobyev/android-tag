package ru.lexxv.tag.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.lexxv.tag.R
import ru.lexxv.tag.components.StatisticsAdapter
import ru.lexxv.tag.database.repository.GameRepository

class StatisticsActivity : ComponentActivity() {
    private lateinit var statsListView: ListView
    private lateinit var emptyTextView: TextView
    private lateinit var backButton: Button
    private lateinit var resetButton: Button
    private lateinit var repository: GameRepository
    private lateinit var adapter: StatisticsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        initViews()
        loadStatistics()
        initListeners()
    }

    private fun initListeners() {
        backButton.setOnClickListener {
            finish()
        }

        resetButton.setOnClickListener {
            resetStatistics()
        }
    }

    private fun initViews() {
        statsListView = findViewById(R.id.statsListView)
        emptyTextView = findViewById(R.id.emptyTextView)
        backButton = findViewById(R.id.backButton)
        resetButton = findViewById(R.id.resetButton)
        repository = GameRepository(this)

        statsListView.emptyView = emptyTextView
    }

    private fun loadStatistics() {
        lifecycleScope.launch(Dispatchers.IO) {
            val results = repository.getAllGameResults()

            withContext(Dispatchers.Main) {
                if (results.isNotEmpty()) {
                    adapter = StatisticsAdapter(this@StatisticsActivity, results)
                    statsListView.adapter = adapter
                }
            }
        }
    }

    private fun resetStatistics() {
        lifecycleScope.launch(Dispatchers.IO) {
            repository.clearGameResults()

            withContext(Dispatchers.Main) {
                adapter.clear()
                adapter.notifyDataSetChanged()
                Toast.makeText(this@StatisticsActivity, "Статистика сброшена", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
