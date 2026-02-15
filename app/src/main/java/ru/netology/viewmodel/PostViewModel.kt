package ru.netology.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.data.Draft
import ru.netology.data.Post
import ru.netology.db.AppDb
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository
    val data: LiveData<List<Post>>
    val draft: LiveData<Draft?>
    private val _editedPost = MutableLiveData<Post?>(null)
    val editedPost: LiveData<Post?> = _editedPost
    private val _navigateToFeed = MutableLiveData<Unit>()
    val navigateToFeed: LiveData<Unit> = _navigateToFeed
    init {
        val db = AppDb.getInstance(application)
        repository = PostRepositoryImpl(db.postDao(), db.draftDao())
        data = repository.getAll()
        draft = repository.getDraft()
    }
    fun save(content: String) {
        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty()) {
            return
        }
        viewModelScope.launch {
            val post = _editedPost.value?.copy(content = trimmedContent)
                ?: Post(
                    author = "Я",
                    content = trimmedContent,
                    published = System.currentTimeMillis()
                )
            repository.save(post)
            clearDraft()
            _editedPost.value = null
            _navigateToFeed.value = Unit
        }
    }
    fun edit(post: Post) {
        _editedPost.value = post
    }
    fun cancelEdit() {
        _editedPost.value = null
    }
    fun removeById(id: Long) {
        viewModelScope.launch {
            repository.removeById(id)
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
    fun saveDraft(content: String) {
        if (content.isNotBlank()) {
            viewModelScope.launch {
                repository.saveDraft(content)
            }
        }
    }
    fun clearDraft() {
        viewModelScope.launch {
            repository.clearDraft()
        }
    }
}