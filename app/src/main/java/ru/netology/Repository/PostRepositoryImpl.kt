package ru.netology.repository
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.dao.DraftDao
import ru.netology.dao.PostDao
import ru.netology.data.Draft
import ru.netology.data.Post
class PostRepositoryImpl(
    private val postDao: PostDao,
    private val draftDao: DraftDao
) : PostRepository {
    override fun getAll(): LiveData<List<Post>> = postDao.getAll()
    override suspend fun save(post: Post) {
        withContext(Dispatchers.IO) {
            if (post.id == 0L) {
                postDao.insert(post)
            } else {
                postDao.update(post)
            }
        }
    }
    override suspend fun removeById(id: Long) {
        withContext(Dispatchers.IO) {
            postDao.removeById(id)
        }
    }
    override suspend fun likeById(id: Long) {
        withContext(Dispatchers.IO) {
            val post = postDao.getById(id) ?: return@withContext
            if (post.likedByMe) {
                postDao.unlikeById(id)
            } else {
                postDao.likeById(id)
            }
        }
    }
    override suspend fun shareById(id: Long) {
        withContext(Dispatchers.IO) {
            postDao.shareById(id)
        }
    }
    override fun getDraft(): LiveData<Draft?> = draftDao.get()
    override suspend fun saveDraft(content: String) {
        withContext(Dispatchers.IO) {
            draftDao.save(Draft(content = content))
        }
    }
    override suspend fun clearDraft() {
        withContext(Dispatchers.IO) {
            draftDao.clear()
        }
    }
}