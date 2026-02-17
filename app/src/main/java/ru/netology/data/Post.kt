package ru.netology.data
data class Post(
    val id: Long = 0,
    val author: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0,
    val videoUrl: String? = null
)