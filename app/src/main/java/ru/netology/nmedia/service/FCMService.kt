package ru.netology.nmedia.service
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.MainActivity
import ru.netology.nmedia.R
import kotlin.random.Random
class FCMService : FirebaseMessagingService() {
    private val gson = Gson()
    private val channelId = "nmedia_notifications"
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    override fun onMessageReceived(message: RemoteMessage) {
        println("📨 Получено сообщение от: ${message.from}")
        println("📦 Данные: ${message.data}")
        // Получаем тип действия из data
        val action = message.data["action"]
        when (action) {
            "LIKE" -> handleLikeNotification(message)
            "NEW_POST" -> handleNewPostNotification(message)
            else -> handleDefaultNotification(message)
        }
    }
    override fun onNewToken(token: String) {
        println("🔑 Новый FCM токен: $token")
        // Здесь можно отправить токен на сервер
    }
    // ================================================
    // ВИД 1: Уведомление о новом посте
    // ================================================
    private fun handleNewPostNotification(message: RemoteMessage) {
        val authorName = message.data["authorName"] ?: "Неизвестный автор"
        val postContent = message.data["postContent"] ?: ""
        val postId = message.data["postId"]?.toLongOrNull() ?: 0L
        println("📝 Новый пост от: $authorName")
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "NEW_POST")
            putExtra("postId", postId)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("📝 Новый пост от $authorName")
            .setContentText(postContent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(postContent)
                    .setBigContentTitle("Новый пост от $authorName")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        showNotification(notification)
    }
    // ================================================
    // ВИД 2: Уведомление о лайке
    // ================================================
    private fun handleLikeNotification(message: RemoteMessage) {
        val likerName = message.data["likerName"] ?: "Кто-то"
        val postId = message.data["postId"]?.toLongOrNull() ?: 0L
        val postPreview = message.data["postPreview"] ?: "ваш пост"
        println("❤️ Лайк от: $likerName")
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "LIKE")
            putExtra("postId", postId)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("❤️ Новый лайк!")
            .setContentText("$likerName лайкнул: $postPreview")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$likerName лайкнул ваш пост:\n$postPreview")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        showNotification(notification)
    }
    // ================================================
    // Обработка стандартных уведомлений
    // ================================================
    private fun handleDefaultNotification(message: RemoteMessage) {
        message.notification?.let { notification ->
            val title = notification.title ?: "Уведомление"
            val body = notification.body ?: ""
            println("🔔 Стандартное уведомление: $title")
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                Random.nextInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
            showNotification(notificationBuilder)
        }
    }
    // ================================================
    // Показ уведомления с проверкой разрешений
    // ================================================
    private fun showNotification(notification: android.app.Notification) {
        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
            println("✅ Уведомление показано")
        } else {
            println("⚠️ Нет разрешения на показ уведомлений")
        }
    }
    // ================================================
    // Проверка разрешения на уведомления
    // ================================================
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
    // ================================================
    // Создание канала уведомлений
    // ================================================
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "NMedia Уведомления"
            val descriptionText = "Уведомления о новых постах и лайках"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            println("📢 Канал уведомлений создан")
        }
    }
}