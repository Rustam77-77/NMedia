package ru.netology
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryImpl()
    val data: LiveData<List<Post>> = repository.getAll()

    private val _editedPost = MutableLiveData<Post?>(null)
    val editedPost: LiveData<Post?> = _editedPost
    fun likeById(id: Long) = repository.likeById(id)

    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) = repository.removeById(id)
    fun save(content: String) {
        val text = content.trim()
        if (text.isEmpty()) return
        val post = _editedPost.value?.copy(content = text) ?: Post(
            id = 0,
            author = "",
            content = text,
            published = ""
        )
        repository.save(post)
        _editedPost.value = null
    }
    fun edit(post: Post) {
        _editedPost.value = post
    }
    fun cancelEdit() {
        _editedPost.value = null
    }
}