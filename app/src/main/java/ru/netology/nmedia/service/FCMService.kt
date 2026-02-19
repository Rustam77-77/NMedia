package ru.netology.nmedia.service
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.netology.nmedia.MainActivity
import ru.netology.nmedia.R
import kotlin.random.Random
class FCMService : FirebaseMessagingService() {
    private val channelId = "new_posts_channel"
    private val tag = "FCMService"
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d(tag, "FCMService создан")
    }
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(tag, "Получено сообщение от: ${message.from}")
        Log.d(tag, "Данные сообщения: ${message.data}")
        val action = message.data["action"]

        when (action) {
            "NEW_POST" -> handleNewPostNotification(message)
            "LIKE" -> handleLikeNotification(message)
            else -> handleDefaultNotification(message)
        }
    }
    override fun onNewToken(token: String) {
        Log.d(tag, "Новый FCM токен: $token")
    }
    private fun handleNewPostNotification(message: RemoteMessage) {
        val authorName = message.data["authorName"] ?: "Пользователь"
        val postContent = message.data["postContent"] ?: ""
        Log.d(tag, "Обработка нового поста от: $authorName")
        val title = "$authorName опубликовал новый пост:"

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("action", "NEW_POST")
            putExtra("authorName", authorName)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(postContent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(postContent)
                    .setBigContentTitle(title)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        // ИСПРАВЛЕНИЕ: Проверка разрешения перед показом уведомления
        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
            Log.d(tag, "Уведомление показано")
        } else {
            Log.w(tag, "Нет разрешения на показ уведомлений")
        }
    }
    private fun handleLikeNotification(message: RemoteMessage) {
        val likerName = message.data["likerName"] ?: "Кто-то"

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Новый лайк")
            .setContentText("$likerName лайкнул ваш пост")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        // ИСПРАВЛЕНИЕ: Проверка разрешения
        if (hasNotificationPermission()) {
            NotificationManagerCompat.from(this).notify(Random.nextInt(), notification)
        }
    }
    private fun handleDefaultNotification(message: RemoteMessage) {
        message.notification?.let { notification ->
            val title = notification.title ?: "Новое уведомление"
            val body = notification.body ?: ""
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
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
            // ИСПРАВЛЕНИЕ: Проверка разрешения
            if (hasNotificationPermission()) {
                NotificationManagerCompat.from(this).notify(Random.nextInt(), notificationBuilder)
            }
        }
    }
    // НОВАЯ ФУНКЦИЯ: Проверка разрешения на уведомления
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Для Android < 13 разрешение не требуется
            true
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            Log.d(tag, "Канал уведомлений создан")
        }
    }
}