package ru.netology.activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.R
import ru.netology.viewmodel.PostViewModel
class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Пример использования ViewModel
        viewModel.data.observe(this) { posts ->
            // Обновление UI с данными
        }
    }
}