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
            likesCount = 999,
            shareCount = 997
        ),
        Post(
            id = nextId++,
            author = "Иван Иванов",
            content = "Второй тестовый пост для демонстрации работы RecyclerView и адаптера с различными обработчиками событий.",
            published = "22 мая в 10:00",
            likedByMe = true,
            likesCount = 1500,
            shareCount = 999
        ),
        Post(
            id = nextId++,
            author = "Пётр Петров",
            content = "Короткий пост",
            published = "23 мая в 15:20",
            likedByMe = false,
            likesCount = 10500,
            shareCount = 5000
        ),
        Post(
            id = nextId++,
            author = "Мария Сидорова",
            content = "Очень длинный пост с большим количеством текста для проверки корректного отображения в карточке. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            published = "24 мая в 09:15",
            likedByMe = true,
            likesCount = 50000,
            shareCount = 12000
        )
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareCount = it.shareCount + 1
            )
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
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }
}