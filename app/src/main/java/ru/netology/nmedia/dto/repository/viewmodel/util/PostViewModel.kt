package ru.netology.nmedia.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data: LiveData<Post> = repository.get()

    fun onLikeClicked() {
        repository.like()
    }

    fun onShareClicked() {
        repository.share()
    }

    fun onViewsClicked() {
        repository.incrementViews()
    }
}