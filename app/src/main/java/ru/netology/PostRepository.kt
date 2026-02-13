package ru.netology
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.netology.db.AppDatabase
import ru.netology.db.PostEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    suspend fun likeById(id: Long)
    suspend fun shareById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
}
class PostRepositoryImpl(
    private val database: AppDatabase
) : PostRepository {
    private val dao = database.postDao()
    override fun getAll(): LiveData<List<Post>> =
        dao.getAll().map { entities ->
            entities.map { it.toModel() }
        }
    override suspend fun likeById(id: Long) {
        withContext(Dispatchers.IO) {
            dao.likeById(id)
        }
    }
    override suspend fun shareById(id: Long) {
        withContext(Dispatchers.IO) {
            dao.shareById(id)
        }
    }
    override suspend fun removeById(id: Long) {
        withContext(Dispatchers.IO) {
            dao.removeById(id)
        }
    }
    override suspend fun save(post: Post) {
        withContext(Dispatchers.IO) {
            if (post.id == 0L) {
                val newPost = post.copy(
                    author = "Me",
                    published = getCurrentDateTime()
                )
                dao.insert(PostEntity.fromModel(newPost))
            } else {
                dao.updateContent(post.id, post.content)
            }
        }
    }
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("d MMMM 'в' HH:mm", Locale("ru"))
        return dateFormat.format(Date())
    }
    suspend fun insertInitialData() {
        withContext(Dispatchers.IO) {
            val posts = listOf(
                PostEntity(
                    id = 1,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                    published = "21 мая в 18:36",
                    likes = 999,
                    shares = 997,
                    views = 5000
                ),
                PostEntity(
                    id = 2,
                    author = "Нетология",
                    content = "Знаний хватит на всех: на следующей неделе разбираемся с основами Android-разработки!",
                    published = "18 сентября в 10:12",
                    likes = 10,
                    shares = 5,
                    views = 150
                )
            )
            dao.insert(posts)
        }
    }
}