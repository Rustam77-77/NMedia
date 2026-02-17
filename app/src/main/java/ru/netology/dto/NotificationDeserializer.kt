package ru.netology.dto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
class NotificationDeserializer : JsonDeserializer<Notification> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Notification {
        val jsonObject = json.asJsonObject

        // Безопасное извлечение полей
        val userId = jsonObject.get("userId")?.asLong ?: 0L
        val userName = jsonObject.get("userName")?.asString ?: "Unknown"
        val content = jsonObject.get("content")?.asString
        val postId = jsonObject.get("postId")?.asLong
        val timestamp = jsonObject.get("timestamp")?.asLong ?: System.currentTimeMillis()

        // Безопасная обработка action
        val actionString = jsonObject.get("action")?.asString
        val action = Action.fromString(actionString)

        return Notification(
            userId = userId,
            userName = userName,
            action = action,
            content = content,
            postId = postId,
            timestamp = timestamp
        )
    }
}