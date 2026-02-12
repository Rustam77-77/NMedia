package ru.netology.repository
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post
class PostRepositoryJson(private val context: Context) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"

    private var nextId = 1L
    private var posts = emptyList<Post>()

    private val data = MutableLiveData(posts)
    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
                nextId = (posts.maxOfOrNull { post -> post.id } ?: 0) + 1
                data.value = posts
            }
        } else {
            // Инициализация начальными данными
            posts = listOf(
                Post(
                    id = nextId++,
                    author = "Нетология. Университет интернет-профессий будущего",
                    content = "Привет, это новая Нетология!",
                    published = "21 мая в 18:36",
                    likes = 999,
                    shares = 997,
                    views = 5
                )
            )
            sync()
        }
    }
    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likes = if (post.likedByMe) post.likes - 1 else post.likes + 1,
                    likedByMe = !post.likedByMe
                )
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }
    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(shares = post.shares + 1)
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }
    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }
    override fun save(post: Post) {
        if (post.id == 0L) {
            // Создание нового поста
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "now"
                )
            ) + posts
        } else {
            // Редактирование существующего
            posts = posts.map {
                if (it.id == post.id) post else it
            }
        }
        data.value = posts
        sync()
    }
    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }
}