package ru.netology.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentNewPostBinding
import ru.netology.viewmodel.PostViewModel
class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        // Восстановление редактируемого поста
        viewModel.editedPost.observe(viewLifecycleOwner) { post ->
            if (post != null) {
                binding.edit.setText(post.content)
                binding.edit.requestFocus()
            }
        }
        // Восстановление черновика при создании нового поста
        if (viewModel.editedPost.value == null) {
            viewModel.draft.observe(viewLifecycleOwner) { draft ->
                if (draft != null && binding.edit.text.isNullOrEmpty()) {
                    binding.edit.setText(draft.content)
                }
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
        // Сохранение черновика при нажатии системной кнопки "Назад"
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val content = binding.edit.text.toString()

            // Если редактируем пост, просто отменяем
            if (viewModel.editedPost.value != null) {
                viewModel.cancelEdit()
                findNavController().navigateUp()
                return@addCallback
            }

            // Если есть текст и это новый пост - сохраняем черновик
            if (content.isNotBlank()) {
                viewModel.saveDraft(content)
            } else {
                viewModel.clearDraft()
            }

            findNavController().navigateUp()
        }
        return binding.root
    }
}