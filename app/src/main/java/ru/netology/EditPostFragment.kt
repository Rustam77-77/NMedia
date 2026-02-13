package ru.netology
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentEditPostBinding
class EditPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(inflater, container, false)
        val initialContent = arguments?.getString("initialContent") ?: ""
        binding.content.setText(initialContent)
        binding.save.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isNotBlank()) {
                viewModel.save(content)
                findNavController().navigateUp()
            }
        }
        binding.cancel.setOnClickListener {
            viewModel.cancelEdit()
            findNavController().navigateUp()
        }
        return binding.root
    }
}