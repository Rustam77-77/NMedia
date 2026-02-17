package ru.netology.service
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ru.netology.MainActivity
import ru.netology.R
import ru.netology.dto.Action
import ru.netology.dto.Notification
import ru.netology.util.GsonHelper
import kotlin.random.Random
class FCMService : Service() {

    private val channelId = "netology_notifications"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Получаем JSON из Intent
        val notificationJson = intent?.getStringExtra(EXTRA_NOTIFICATION_JSON)

        if (!notificationJson.isNullOrBlank()) {
            processNotification(notificationJson)
        }

        return START_NOT_STICKY
    }

    /**
     * Обработка уведомления
     */
    private fun processNotification(notificationJson: String) {
        android.util.Log.d(TAG, "Получено уведомление: $notificationJson")

        // Безопасная десериализация
        val notification = GsonHelper.parseNotification(notificationJson)

        if (notification == null) {
            android.util.Log.e(TAG, "Не удалось распарсить уведомление: $notificationJson")
            handleInvalidNotification(notificationJson)
            return
        }

        // Проверка валидности
        if (!notification.isValid()) {
            android.util.Log.w(TAG, "Получено невалидное уведомление: $notification")
            return
        }

        // Отображение уведомления
        showNotification(notification)
    }

    /**
     * Обработка невалидного уведомления
     */
    private fun handleInvalidNotification(json: String) {
        android.util.Log.e(TAG, "Invalid notification received: $json")
        showGenericNotification()
    }

    /**
     * Отображение push-уведомления
     */
    private fun showNotification(notification: Notification) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("userId", notification.userId)
            putExtra("postId", notification.postId)
            putExtra("action", notification.action.name)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getTitleForAction(notification.action))
            .setContentText(notification.getDisplayText())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random.nextInt(), notificationBuilder.build())

        android.util.Log.i(TAG, "Уведомление показано: ${notification.action}")
    }

    /**
     * Показ общего уведомления для неизвестных action
     */
    private fun showGenericNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Новое уведомление")
            .setContentText("У вас есть новое уведомление")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random.nextInt(), notification)
    }

    /**
     * Создание канала уведомлений (Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Уведомления Netology"
            val descriptionText = "Уведомления о активности в приложении"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Получение заголовка для типа действия
     */
    private fun getTitleForAction(action: Action): String {
        return when (action) {
            Action.LIKE -> "Новый лайк"
            Action.COMMENT -> "Новый комментарий"
            Action.SHARE -> "Поделились постом"
            Action.POST -> "Новый пост"
            Action.MENTION -> "Вас упомянули"
            Action.UNKNOWN -> "Неизвестное действие"
        }
    }

    companion object {
        private const val TAG = "FCMService"
        private const val EXTRA_NOTIFICATION_JSON = "notification_json"

        /**
         * Вспомогательный метод для отправки тестового уведомления
         */
        fun sendTestNotification(context: Context, notificationJson: String) {
            android.util.Log.d(TAG, "Отправка тестового уведомления: $notificationJson")

            val intent = Intent(context, FCMService::class.java).apply {
                putExtra(EXTRA_NOTIFICATION_JSON, notificationJson)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}