package ru.lexxv.tag.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.lexxv.tag.R
import ru.lexxv.tag.database.table.GameStats

class StatisticsAdapter(context: Context, private val statsList: List<GameStats>) :
    ArrayAdapter<GameStats>(context, R.layout.item_statistics, statsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_statistics, parent, false)

        val movesTextView: TextView = view.findViewById(R.id.movesTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)

        val stat = statsList[position]
        movesTextView.text = "Ходы: ${stat.moves}"
        timeTextView.text = "Время: ${String.format("%.1f", stat.time)} сек"

        return view
    }
}