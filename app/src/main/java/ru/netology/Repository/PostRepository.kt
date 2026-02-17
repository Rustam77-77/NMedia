package ru.netology.repository
import androidx.lifecycle.LiveData
import ru.netology.data.Draft
import ru.netology.data.Post
interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun save(post: Post)
    fun removeById(id: Long)
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun getDraft(): LiveData<Draft?>
    fun saveDraft(content: String)
    fun clearDraft()
}