package ru.netology.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryJson
class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryJson(application)

    val data: LiveData<List<Post>> = repository.getAll()

    private val _editedPost = MutableLiveData<Post?>(null)
    val editedPost: LiveData<Post?> = _editedPost
    fun likeById(id: Long) {
        repository.likeById(id)
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun save(post: Post) {
        repository.save(post)
        _editedPost.value = null
    }

    fun edit(post: Post) {
        _editedPost.value = post
    }

    fun cancelEdit() {
        _editedPost.value = null
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (_editedPost.value?.content == text) {
            return
        }
        _editedPost.value = _editedPost.value?.copy(content = text)
    }
}