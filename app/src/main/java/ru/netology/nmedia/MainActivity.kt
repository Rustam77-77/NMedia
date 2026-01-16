package ru.netology.nmedia
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var likesCount = 999
    private var sharesCount = 997
    private var viewsCount = 5432
    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI()

        binding.like.setOnClickListener {
            isLiked = !isLiked

            if (isLiked) {
                likesCount++
                binding.like.setImageResource(R.drawable.ic_liked_24)
            } else {
                likesCount--
                binding.like.setImageResource(R.drawable.ic_like_24)
            }

            binding.likesCount.text = formatCount(likesCount)
        }

        binding.share.setOnClickListener {
            sharesCount++
            binding.sharesCount.text = formatCount(sharesCount)
        }
    }

    private fun updateUI() {
        binding.likesCount.text = formatCount(likesCount)
        binding.sharesCount.text = formatCount(sharesCount)
        binding.viewsCount.text = formatCount(viewsCount)

        if (isLiked) {
            binding.like.setImageResource(R.drawable.ic_liked_24)
        } else {
            binding.like.setImageResource(R.drawable.ic_like_24)
        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count < 1_000 -> count.toString()
            count < 10_000 -> {
                val thousands = count / 1_000
                val hundreds = (count % 1_000) / 100
                "${thousands}.${hundreds}K"
            }
            count < 1_000_000 -> "${count / 1_000}K"
            count < 10_000_000 -> {
                val millions = count / 1_000_000
                val hundredThousands = (count % 1_000_000) / 100_000
                "${millions}.${hundredThousands}M"
            }
            else -> "${count / 1_000_000}M"
        }
    }
}