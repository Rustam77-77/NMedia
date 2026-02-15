package ru.netology.data
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "drafts")
data class Draft(
    @PrimaryKey
    val id: Int = 1,
    val content: String,
    val savedAt: Long = System.currentTimeMillis()
)