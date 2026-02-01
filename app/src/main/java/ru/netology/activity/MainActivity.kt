package ru.netology.nmedia.activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels()

    private val editPostLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra(PostEditActivity.EXTRA_CONTENT)?.let { content ->
                viewModel.changeContent(content)
                viewModel.save()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(
                    Intent(this@MainActivity, PostEditActivity::class.java).apply {
                        putExtra(PostEditActivity.EXTRA_CONTENT, post.content)
                    }
                )
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            editPostLauncher.launch(Intent(this, PostEditActivity::class.java))
        }
    }
}