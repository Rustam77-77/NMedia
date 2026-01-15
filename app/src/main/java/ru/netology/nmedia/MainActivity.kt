package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению.",
        published = "21 мая в 18:36",
        likes = 999,
        shares = 997,
        views = 5432,
        likedByMe = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        render()

        // Обработчик клика на лайк
        binding.like.setOnClickListener {
            post = if (post.likedByMe) {
                post.copy(likes = post.likes - 1, likedByMe = false)
            } else {
                post.copy(likes = post.likes + 1, likedByMe = true)
            }
            render()
        }

        // Обработчик клика на share
        binding.share.setOnClickListener {
            post = post.copy(shares = post.shares + 1)
            render()
        }
    }

    private fun render() {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            // Меняем иконку лайка в зависимости от состояния
            like.setImageResource(
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )

            // Форматируем и отображаем счётчики
            likesCount.text = formatCount(post.likes)
            sharesCount.text = formatCount(post.shares)
            viewsCount.text = formatCount(post.views)
        }
    }
}