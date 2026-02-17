package ru.netology.data
data class Draft(
    val id: Int = 1,
    val content: String,
    val savedAt: Long = System.currentTimeMillis()
)