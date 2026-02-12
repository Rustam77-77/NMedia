package ru.netology.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.dto.Post
interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
}
class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_post, parent, false)
        return PostViewHolder(view, onInteractionListener)
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}
class PostViewHolder(
    itemView: View,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(itemView) {
    private val author: TextView = itemView.findViewById(R.id.author)
    private val published: TextView = itemView.findViewById(R.id.published)
    private val content: TextView = itemView.findViewById(R.id.content)
    private val like: ImageButton = itemView.findViewById(R.id.like)
    private val likes: TextView = itemView.findViewById(R.id.likes)
    private val share: ImageButton = itemView.findViewById(R.id.share)
    private val shares: TextView = itemView.findViewById(R.id.shares)
    private val views: TextView = itemView.findViewById(R.id.views)
    private val menu: ImageButton = itemView.findViewById(R.id.menu)
    fun bind(post: Post) {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        likes.text = formatCount(post.likes)
        shares.text = formatCount(post.shares)
        views.text = formatCount(post.views)
        like.setImageResource(
            if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
        )
        like.setOnClickListener {
            onInteractionListener.onLike(post)
        }
        share.setOnClickListener {
            onInteractionListener.onShare(post)
        }
        menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.edit -> {
                            onInteractionListener.onEdit(post)
                            true
                        }
                        R.id.remove -> {
                            onInteractionListener.onRemove(post)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }
    }
    private fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
            count >= 10_000 -> String.format("%dK", count / 1000)
            count >= 1_000 -> String.format("%.1fK", count / 1000.0)
            else -> count.toString()
        }
    }
}
class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}