package ru.netology.util
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ru.netology.dto.Notification
import ru.netology.dto.NotificationDeserializer
object GsonHelper {

    /**
     * Настроенный Gson с кастомным десериализатором
     */
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Notification::class.java, NotificationDeserializer())
        .setLenient()
        .create()

    /**
     * Безопасная десериализация уведомления
     * @param json JSON-строка
     * @return Notification или null в случае ошибки
     */
    fun parseNotification(json: String): Notification? {
        return try {
            val notification = gson.fromJson(json, Notification::class.java)
            // Дополнительная валидация
            if (notification.isValid()) notification else null
        } catch (e: Exception) {
            android.util.Log.e("GsonHelper", "Ошибка парсинга уведомления", e)
            null
        }
    }
}