package ru.netology.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
class PostRepositoryInMemory : PostRepository {

    private var nextId = 1L

    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 999,
            shares = 997,
            views = 5
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаковое событие для нас: мы запускаем новый курс Java-разработчик! Курс для тех, кто хочет освоить востребованную профессию с нуля.",
            published = "18 сентября в 10:12",
            likes = 1_200_000,
            shares = 1_000_000,
            views = 50_000_000
        )
    )

    private val data = MutableLiveData(posts)
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
    }
    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
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
    }
}