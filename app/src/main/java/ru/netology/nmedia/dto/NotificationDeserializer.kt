package ru.netology.nmedia.dto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
class NotificationDeserializer : JsonDeserializer<Notification> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Notification {
        val jsonObject = json.asJsonObject

        return Notification(
            userId = jsonObject.get("userId").asLong,
            userName = jsonObject.get("userName").asString,
            action = Action.fromString(  // ← используется fromString
                jsonObject.get("action").asString
            ) ?: Action.NEW_POST,  // ← значение по умолчанию если null
            content = jsonObject.get("content")?.asString,
            postId = jsonObject.get("postId")?.asLong
        )
    }
}