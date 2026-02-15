package ru.netology.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.data.Post
@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<Post>>
    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getById(id: Long): Post?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post): Long
    @Update
    suspend fun update(post: Post)
    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun removeById(id: Long)
    @Query("UPDATE posts SET likes = likes + 1, likedByMe = 1 WHERE id = :id")
    suspend fun likeById(id: Long)
    @Query("UPDATE posts SET likes = likes - 1, likedByMe = 0 WHERE id = :id")
    suspend fun unlikeById(id: Long)
    @Query("UPDATE posts SET shares = shares + 1 WHERE id = :id")
    suspend fun shareById(id: Long)
}
