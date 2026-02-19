package ru.netology.nmedia.dto  // ← ИСПРАВЬТЕ с ru.netology.dto
data class Notification(
    val userId: Long,
    val userName: String,
    val action: Action,  // ← теперь Action будет найден
    val content: String? = null,
    val postId: Long? = null
)