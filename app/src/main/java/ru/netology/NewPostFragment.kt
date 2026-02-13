package ru.netology
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentNewPostBinding
class NewPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        // Загружаем черновик, если он есть
        val draft = viewModel.getDraft()
        if (!draft.isNullOrEmpty()) {
            binding.content.setText(draft)
            binding.content.setSelection(draft.length) // Курсор в конец
        }
        // Обработка кнопки "Сохранить"
        binding.save.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isNotBlank()) {
                viewModel.save(content)
                viewModel.clearDraft() // Очищаем черновик
                findNavController().navigateUp()
            }
        }
        // Обработка кнопки "Отмена"
        binding.cancel.setOnClickListener {
            viewModel.clearDraft() // Очищаем черновик
            findNavController().navigateUp()
        }
        // Обработка системной кнопки "Назад"
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val content = binding.content.text.toString()
                    if (content.isNotBlank()) {
                        viewModel.saveDraft(content) // Сохраняем черновик
                    } else {
                        viewModel.clearDraft() // Если пусто - очищаем
                    }
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        )
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}