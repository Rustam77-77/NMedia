package ru.netology.nmedia
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    // Лаунчер для запроса разрешения на уведомления
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "✅ Уведомления разрешены", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "⚠️ Уведомления отклонены", Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Запрос разрешения на уведомления
        requestNotificationPermission()
        // Получение FCM токена
        getFCMToken()
        // Обработка intent из уведомления
        handleNotificationIntent()
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    println("✅ Разрешение на уведомления уже есть")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    Toast.makeText(
                        this,
                        "Разрешите уведомления для получения новостей о постах",
                        Toast.LENGTH_LONG
                    ).show()
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                println("❌ Ошибка получения токена: ${task.exception}")
                return@addOnCompleteListener
            }
            val token = task.result
            println("🔑 FCM Token: $token")

            // Можно показать токен пользователю для тестирования
            Toast.makeText(this, "Токен скопирован в лог", Toast.LENGTH_SHORT).show()
        }
    }
    private fun handleNotificationIntent() {
        val action = intent.getStringExtra("action")
        val postId = intent.getLongExtra("postId", 0L)
        when (action) {
            "NEW_POST" -> {
                println("📝 Открыт из уведомления о новом посте: $postId")
                // Здесь можно открыть конкретный пост
            }
            "LIKE" -> {
                println("❤️ Открыт из уведомления о лайке: $postId")
                // Здесь можно открыть пост с лайком
            }
        }
    }
}