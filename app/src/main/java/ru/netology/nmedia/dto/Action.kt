package ru.netology.nmedia.dto
import com.google.gson.annotations.SerializedName
enum class Action {
    @SerializedName("LIKE")
    LIKE,

    @SerializedName("NEW_POST")
    NEW_POST,

    @SerializedName("SHARE")
    SHARE,

    @SerializedName("REMOVE")
    REMOVE,

    @SerializedName("EDIT")
    EDIT;

    companion object {
        fun fromString(value: String): Action {
            return when (value.uppercase()) {
                "LIKE" -> LIKE
                "NEW_POST" -> NEW_POST
                "SHARE" -> SHARE
                "REMOVE" -> REMOVE
                "EDIT" -> EDIT
                else -> throw IllegalArgumentException("Unknown action: $value")
            }
        }
    }
}