package ru.netology
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.db.AppDatabase
class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository: PostRepository = PostRepositoryImpl(database)
    val data: LiveData<List<Post>> = repository.getAll()

    private val _editedPost = MutableLiveData<Post?>(null)
    val editedPost: LiveData<Post?> = _editedPost
    init {
        viewModelScope.launch {
            // Проверяем, есть ли данные, если нет — добавляем начальные
            if (data.value.isNullOrEmpty()) {
                (repository as? PostRepositoryImpl)?.insertInitialData()
            }
        }
    }
    fun likeById(id: Long) {
        viewModelScope.launch {
            repository.likeById(id)
        }
    }

    fun shareById(id: Long) {
        viewModelScope.launch {
            repository.shareById(id)
        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            repository.removeById(id)
        }
    }
    fun save(content: String) {
        val text = content.trim()
        if (text.isEmpty()) return
        viewModelScope.launch {
            val post = _editedPost.value?.copy(content = text) ?: Post(
                id = 0,
                author = "",
                content = text,
                published = ""
            )
            repository.save(post)
            _editedPost.value = null
        }
    }
    fun edit(post: Post) {
        _editedPost.value = post
    }
    fun cancelEdit() {
        _editedPost.value = null
    }
}