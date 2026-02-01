package ru.netology.nmedia.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
class PostRepositoryInMemoryImpl : PostRepository {

    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 997,
            video = "https://rutube.ru/video/f8b0e7e0b5f6b8c7a1d2e3f4g5h6i7j8/"
        ),
        Post(
            id = nextId++,
            author = "Разработчик Android",
            content = "Изучаем Android разработку вместе!",
            published = "22 мая в 10:00",
            likedByMe = false,
            likes = 150,
            shares = 50,
            video = null  // Пост без видео
        )
    )

    private val data = MutableLiveData(posts)
    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }
    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }
    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }
    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Я",
                    published = "Только что"
                )
            ) + posts
        } else {
            posts = posts.map {
                if (it.id != post.id) it else it.copy(
                    content = post.content,
                    video = post.video
                )
            }
        }
        data.value = posts
    }
}