package ru.netology.nmedia.util
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ru.netology.nmedia.dto.Notification
import ru.netology.nmedia.dto.NotificationDeserializer
object GsonHelper {

    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Notification::class.java, NotificationDeserializer())
        .create()

    fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    fun toJson(src: Any): String {
        return gson.toJson(src)
    }
}