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
    // Черновик для нового поста
    private val _draftContent = MutableLiveData<String?>()
    val draftContent: LiveData<String?> = _draftContent
    init {
        viewModelScope.launch {
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
            _draftContent.value = null // Очищаем черновик после сохранения
        }
    }
    fun edit(post: Post) {
        _editedPost.value = post
    }
    fun cancelEdit() {
        _editedPost.value = null
    }
    // Сохранить черновик
    fun saveDraft(content: String) {
        _draftContent.value = content
    }
    // Очистить черновик
    fun clearDraft() {
        _draftContent.value = null
    }
    // Получить черновик
    fun getDraft(): String? = _draftContent.value
}