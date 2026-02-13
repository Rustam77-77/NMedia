package ru.netology.db
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.Post
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: Int = 0,
    val shares: Int = 0,
    val views: Int = 0
) {
    fun toModel() = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        shares = shares,
        views = views
    )
    companion object {
        fun fromModel(post: Post) = PostEntity(
            id = post.id,
            author = post.author,
            content = post.content,
            published = post.published,
            likedByMe = post.likedByMe,
            likes = post.likes,
            shares = post.shares,
            views = post.views
        )
    }
}