package ru.netology.dto
import com.google.gson.annotations.SerializedName
enum class Action {
    @SerializedName("LIKE")
    LIKE,

    @SerializedName("COMMENT")
    COMMENT,

    @SerializedName("SHARE")
    SHARE,

    @SerializedName("POST")
    POST,

    @SerializedName("MENTION")
    MENTION,

    // Значение по умолчанию для неизвестных action
    @SerializedName("UNKNOWN")
    UNKNOWN;

    companion object {
        /**
         * Безопасное получение Action из строки
         * @param value строковое значение action
         * @return соответствующий Action или UNKNOWN
         */
        fun fromString(value: String?): Action {
            return try {
                values().find {
                    it.name.equals(value, ignoreCase = true)
                } ?: UNKNOWN
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }
}