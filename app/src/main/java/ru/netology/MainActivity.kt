package ru.netology
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Action
import ru.netology.service.FCMService
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        handleNotificationIntent(intent)
    }

    override fun onNewIntent(intent: android.content.Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        intent?.let { handleNotificationIntent(it) }
    }

    /**
     * Настройка UI элементов
     */
    private fun setupUI() {
        binding.textNotificationInfo.text = "Ожидание уведомлений..."

        binding.buttonClear.setOnClickListener {
            clearNotificationInfo()
        }

        binding.buttonTestNotification.setOnClickListener {
            sendTestNotification()
        }
    }

    /**
     * Обработка Intent от уведомления
     */
    private fun handleNotificationIntent(intent: android.content.Intent) {
        val userId = intent.getLongExtra("userId", 0L)
        val postId = intent.getLongExtra("postId", 0L)
        val actionName = intent.getStringExtra("action")

        Log.d(TAG, "handleNotificationIntent: userId=$userId, postId=$postId, action=$actionName")

        if (userId == 0L || actionName.isNullOrBlank()) {
            Log.w(TAG, "Некорректные параметры Intent")
            return
        }

        val action = Action.fromString(actionName)

        // Отображение информации о уведомлении
        displayNotificationInfo(action, userId, postId)

        // Обработка действия
        when (action) {
            Action.LIKE -> handleLikeAction(userId, postId)
            Action.COMMENT -> handleCommentAction(userId, postId)
            Action.SHARE -> handleShareAction(userId, postId)
            Action.POST -> handlePostAction(userId, postId)
            Action.MENTION -> handleMentionAction(userId, postId)
            Action.UNKNOWN -> handleUnknownAction(userId)
        }
    }

    /**
     * Отображение информации о полученном уведомлении
     */
    private fun displayNotificationInfo(action: Action, userId: Long, postId: Long) {
        val info = buildString {
            appendLine("Получено уведомление:")
            appendLine("Тип: ${getActionDisplayName(action)}")
            appendLine("Пользователь ID: $userId")
            if (postId > 0) {
                appendLine("Пост ID: $postId")
            }
            appendLine("Время: ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}")
        }

        binding.textNotificationInfo.text = info
        binding.cardNotificationInfo.isVisible = true

        Log.i(TAG, "Отображена информация: $info")
    }

    /**
     * Получение читаемого имени действия
     */
    private fun getActionDisplayName(action: Action): String {
        return when (action) {
            Action.LIKE -> "Лайк"
            Action.COMMENT -> "Комментарий"
            Action.SHARE -> "Поделились"
            Action.POST -> "Новый пост"
            Action.MENTION -> "Упоминание"
            Action.UNKNOWN -> "Неизвестное действие"
        }
    }

    /**
     * Обработка лайка
     */
    private fun handleLikeAction(userId: Long, postId: Long) {
        showToast("Пользователь #$userId поставил лайк на пост #$postId")
        updateCurrentAction("Открыт пост #$postId с подсветкой лайка")
        Log.d(TAG, "handleLikeAction: userId=$userId, postId=$postId")
    }

    /**
     * Обработка комментария
     */
    private fun handleCommentAction(userId: Long, postId: Long) {
        showToast("Новый комментарий от пользователя #$userId к посту #$postId")
        updateCurrentAction("Открыт пост #$postId с прокруткой к комментариям")
        Log.d(TAG, "handleCommentAction: userId=$userId, postId=$postId")
    }

    /**
     * Обработка поделились постом
     */
    private fun handleShareAction(userId: Long, postId: Long) {
        showToast("Пользователь #$userId поделился постом #$postId")
        updateCurrentAction("Открыт пост #$postId с информацией о шеринге")
        Log.d(TAG, "handleShareAction: userId=$userId, postId=$postId")
    }

    /**
     * Обработка нового поста
     */
    private fun handlePostAction(userId: Long, postId: Long) {
        showToast("Пользователь #$userId опубликовал новый пост #$postId")
        updateCurrentAction("Открыт новый пост #$postId")
        Log.d(TAG, "handlePostAction: userId=$userId, postId=$postId")
    }

    /**
     * Обработка упоминания
     */
    private fun handleMentionAction(userId: Long, postId: Long) {
        showToast("Вас упомянул пользователь #$userId в посте #$postId")
        updateCurrentAction("Открыт пост #$postId с подсветкой упоминания")
        Log.d(TAG, "handleMentionAction: userId=$userId, postId=$postId")
    }

    /**
     * Обработка неизвестного действия
     */
    private fun handleUnknownAction(userId: Long) {
        showToast("Получено уведомление с неизвестным типом от пользователя #$userId")
        updateCurrentAction("Открыта главная лента")
        Log.w(TAG, "handleUnknownAction: userId=$userId - неизвестный тип действия")
    }

    /**
     * Обновление текста текущего действия
     */
    private fun updateCurrentAction(text: String) {
        binding.textCurrentAction.text = text
        binding.textCurrentAction.isVisible = true
        Log.i(TAG, text)
    }

    /**
     * Показ Toast сообщения
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Очистка информации о уведомлении
     */
    private fun clearNotificationInfo() {
        binding.cardNotificationInfo.isVisible = false
        binding.textCurrentAction.isVisible = false
        binding.textNotificationInfo.text = "Ожидание уведомлений..."
        Log.d(TAG, "Информация о уведомлении очищена")
    }

    /**
     * Отправка тестового уведомления
     */
    private fun sendTestNotification() {
        val testJson = """
            {
                "userId": 123,
                "userName": "Тестовый пользователь",
                "action": "LIKE",
                "postId": 456,
                "timestamp": ${System.currentTimeMillis()}
            }
        """.trimIndent()

        FCMService.sendTestNotification(this, testJson)
        showToast("Тестовое уведомление отправлено")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("notification_info", binding.textNotificationInfo.text)
        outState.putCharSequence("current_action", binding.textCurrentAction.text)
        outState.putBoolean("info_visible", binding.cardNotificationInfo.isVisible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.textNotificationInfo.text = savedInstanceState.getCharSequence("notification_info")
        binding.textCurrentAction.text = savedInstanceState.getCharSequence("current_action")
        binding.cardNotificationInfo.isVisible = savedInstanceState.getBoolean("info_visible", false)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}