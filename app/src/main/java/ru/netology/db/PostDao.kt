package ru.netology.db
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)
    @Query("UPDATE posts SET likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END, likedByMe = NOT likedByMe WHERE id = :id")
    suspend fun likeById(id: Long)
    @Query("UPDATE posts SET shares = shares + 1 WHERE id = :id")
    suspend fun shareById(id: Long)
    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun removeById(id: Long)
    @Query("UPDATE posts SET content = :content WHERE id = :id")
    suspend fun updateContent(id: Long, content: String)
}