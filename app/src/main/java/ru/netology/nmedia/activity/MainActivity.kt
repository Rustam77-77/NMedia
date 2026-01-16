package ru.netology.nmedia.activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.NumberUtils
import ru.netology.nmedia.viewmodel.PostViewModel
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        // Наблюдаем за изменениями данных
        viewModel.data.observe(this) { post ->
            with(binding) {
                // Отображаем данные поста
                authorTextView.text = post.author
                contentTextView.text = post.content
                publishedTextView.text = post.published

                // Форматируем и отображаем счётчики
                likesCountTextView.text = NumberUtils.formatCount(post.likes)
                sharesCountTextView.text = NumberUtils.formatCount(post.reposts)
                viewsCountTextView.text = NumberUtils.formatCount(post.views)

                // Меняем иконку лайка в зависимости от состояния
                likeButton.setIconResource(
                    if (post.likedByMe) R.drawable.ic_liked_24
                    else R.drawable.ic_like_24
                )

                // Меняем иконку репоста в зависимости от состояния
                shareButton.setIconResource(
                    if (post.repostedByMe) R.drawable.ic_shared_24
                    else R.drawable.ic_share_24
                )
            }
        }

        // Обработчик нажатия на кнопку лайка
        binding.likeButton.setOnClickListener {
            viewModel.onLikeClicked()
        }

        // Обработчик нажатия на кнопку репоста
        binding.shareButton.setOnClickListener {
            viewModel.onShareClicked()
        }

        // Обработчик нажатия на кнопку просмотров
        binding.viewsButton.setOnClickListener {
            viewModel.onViewsClicked()
        }
    }
}