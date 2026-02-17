package ru.netology.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dao.DraftDao
import ru.netology.dao.PostDao
import ru.netology.data.Draft
import ru.netology.data.Post
import java.util.concurrent.Executors
class PostRepositoryImpl(
    private val postDao: PostDao,
    private val draftDao: DraftDao
) : PostRepository {
    private val executor = Executors.newSingleThreadExecutor()

    private val _data = MutableLiveData<List<Post>>()
    override fun getAll(): LiveData<List<Post>> = _data
    private val _draft = MutableLiveData<Draft?>()
    override fun getDraft(): LiveData<Draft?> = _draft
    init {
        loadPosts()
        loadDraft()
    }
    private fun loadPosts() {
        executor.execute {
            val posts = postDao.getAll()
            _data.postValue(posts)
        }
    }
    private fun loadDraft() {
        executor.execute {
            val draft = draftDao.get()
            _draft.postValue(draft)
        }
    }
    override fun save(post: Post) {
        executor.execute {
            if (post.id == 0L) {
                postDao.insert(post)
            } else {
                postDao.update(post)
            }
            loadPosts()
        }
    }
    override fun removeById(id: Long) {
        executor.execute {
            postDao.removeById(id)
            loadPosts()
        }
    }
    override fun likeById(id: Long) {
        executor.execute {
            val post = postDao.getById(id) ?: return@execute
            if (post.likedByMe) {
                postDao.unlikeById(id)
            } else {
                postDao.likeById(id)
            }
            loadPosts()
        }
    }
    override fun shareById(id: Long) {
        executor.execute {
            postDao.shareById(id)
            loadPosts()
        }
    }
    override fun saveDraft(content: String) {
        executor.execute {
            draftDao.save(Draft(content = content))
            loadDraft()
        }
    }
    override fun clearDraft() {
        executor.execute {
            draftDao.clear()
            loadDraft()
        }
    }
}