package ru.netology.nmedia.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.nmedia.entity.PostEntity
@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>
    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getById(id: Long): PostEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PostEntity)
    @Update
    fun update(post: PostEntity)
    @Query("""
        UPDATE PostEntity SET 
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END, 
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END 
        WHERE id = :id
    """)
    fun likeById(id: Long)
    @Query("UPDATE PostEntity SET shares = shares + 1 WHERE id = :id")
    fun shareById(id: Long)
    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)
    @Query("DELETE FROM PostEntity")
    fun removeAll()
}