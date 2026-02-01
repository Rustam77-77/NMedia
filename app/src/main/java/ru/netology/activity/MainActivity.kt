package ru.netology.nmedia.activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
            result.data?.let { data ->
                val content = data.getStringExtra(PostEditActivity.EXTRA_CONTENT)
                val video = data.getStringExtra(PostEditActivity.EXTRA_VIDEO)

                content?.let {
                    viewModel.changeContent(it)
                    viewModel.changeVideo(video)
                    viewModel.save()
                }
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

                val intent = Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    },
                    getString(R.string.share_description)
                )

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.share_success,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(
                    Intent(this@MainActivity, PostEditActivity::class.java).apply {
                        putExtra(PostEditActivity.EXTRA_CONTENT, post.content)
                        post.video?.let { putExtra(PostEditActivity.EXTRA_VIDEO, it) }
                    }
                )
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun onPlayVideo(post: Post) {
                post.video?.let { videoUrl ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            R.string.error_play_video,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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