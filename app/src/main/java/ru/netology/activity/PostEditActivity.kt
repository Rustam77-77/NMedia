package ru.netology.nmedia.activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostEditBinding
class PostEditActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_CONTENT = "content"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val initialContent = intent.getStringExtra(EXTRA_CONTENT)

        binding.edit.apply {
            setText(initialContent)
            requestFocus()
        }
        title = if (initialContent != null) {
            getString(R.string.edit)
        } else {
            getString(R.string.add_post)
        }
        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            if (content.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_CONTENT, content)
                }
                setResult(RESULT_OK, resultIntent)
            }
            finish()
        }
        binding.cancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}