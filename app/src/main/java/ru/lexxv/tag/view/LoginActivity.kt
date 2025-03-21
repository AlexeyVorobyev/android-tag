package ru.lexxv.tag.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import ru.lexxv.tag.R
import ru.lexxv.tag.view.game.GameActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Инициализация элементов интерфейса
        inputField = findViewById(R.id.inputField)
        loginButton = findViewById(R.id.loginButton)

        // Обработка нажатия на кнопку
        loginButton.setOnClickListener {
            val inputText = inputField.text.toString()
            if (inputText.isNotEmpty()) {
                // Создаем Intent для перехода на GameActivity
                val intent = Intent(this, GameActivity::class.java).apply {
                    // Передаем строку через Intent
                    putExtra("INPUT_TEXT", inputText)
                }
                startActivity(intent)
                finish()
            } else {
                showToast("Поле ввода пустое!")
            }
        }
    }

    // Вспомогательная функция для отображения Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}