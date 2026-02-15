package ru.netology.repository
import androidx.lifecycle.LiveData
import ru.netology.data.Draft
import ru.netology.data.Post
interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun shareById(id: Long)
    fun getDraft(): LiveData<Draft?>
    suspend fun saveDraft(content: String)
    suspend fun clearDraft()
}