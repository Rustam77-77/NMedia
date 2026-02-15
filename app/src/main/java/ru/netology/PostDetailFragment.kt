package ru.netology
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentPostDetailBinding
class PostDetailFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        val postId = arguments?.getLong("postId") ?: 0L
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content
            binding.like.setImageResource(
                if (post.likedByMe) R.drawable.ic_favorite_24
                else R.drawable.ic_favorite_border_24
            )
            binding.likeCount.text = formatCount(post.likes)
            binding.shareCount.text = formatCount(post.shares)
            binding.viewsCount.text = formatCount(post.views)
            binding.like.setOnClickListener {
                viewModel.likeById(post.id)
            }
            binding.share.setOnClickListener {
                viewModel.shareById(post.id)
            }
            binding.menu.setOnClickListener {
                showPopupMenu(it, post)
            }
        }
        return binding.root
    }
    private fun showPopupMenu(view: View, post: Post) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.post_options_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.remove -> {
                        viewModel.removeById(post.id)
                        findNavController().navigateUp()
                        true
                    }
                    R.id.edit -> {
                        viewModel.edit(post)
                        findNavController().navigate(
                            R.id.action_postDetailFragment_to_editPostFragment,
                            bundleOf("initialContent" to post.content)
                        )
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }
    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 10_000 -> "${count / 1000}K"
            count >= 1_000 -> String.format("%.1fK", count / 1000.0)
            else -> count.toString()
        }
    }
}