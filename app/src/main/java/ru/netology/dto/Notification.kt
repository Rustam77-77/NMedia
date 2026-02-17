package ru.netology.dto
import com.google.gson.annotations.SerializedName
data class Notification(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("action")
    val action: Action = Action.UNKNOWN, // Значение по умолчанию

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("postId")
    val postId: Long? = null,

    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Проверка, является ли уведомление валидным
     */
    fun isValid(): Boolean {
        return action != Action.UNKNOWN && userId > 0 && userName.isNotBlank()
    }

    /**
     * Получение текста уведомления для отображения
     */
    fun getDisplayText(): String {
        return when (action) {
            Action.LIKE -> "$userName поставил(а) лайк"
            Action.COMMENT -> "$userName оставил(а) комментарий: ${content ?: ""}"
            Action.SHARE -> "$userName поделился(лась) вашим постом"
            Action.POST -> "$userName опубликовал(а) новый пост"
            Action.MENTION -> "$userName упомянул(а) вас в посте"
            Action.UNKNOWN -> "Новое уведомление от $userName"
        }
    }
}