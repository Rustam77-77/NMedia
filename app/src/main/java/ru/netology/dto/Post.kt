package ru.netology.dto
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,  // Должна быть String, не Long
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val likedByMe: Boolean = false
)