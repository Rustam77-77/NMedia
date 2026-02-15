package ru.netology.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentEditPostBinding
import ru.netology.viewmodel.PostViewModel
class EditPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(inflater, container, false)
        // Восстановление редактируемого поста
        viewModel.editedPost.observe(viewLifecycleOwner) { post ->
            if (post != null) {
                binding.edit.setText(post.content)
                binding.edit.requestFocus()
            }
        }
        // Сохранение поста
        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            if (content.isNotBlank()) {
                viewModel.save(content)
            }
        }
        // Обработка навигации назад
        viewModel.navigateToFeed.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        // Обработка системной кнопки "Назад"
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.cancelEdit()
            findNavController().navigateUp()
        }
        return binding.root
    }
}