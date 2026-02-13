package ru.netology
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.databinding.FragmentEditPostBinding
class EditPostFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private var initialContent: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        initialContent = arguments?.getString("initialContent") ?: ""
        binding.content.setText(initialContent)
        binding.content.setSelection(initialContent.length)
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
        // Обработка системной кнопки "Назад" при редактировании
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val currentContent = binding.content.text.toString()
                    // Если содержимое изменилось, можно показать диалог подтверждения
                    if (currentContent != initialContent && currentContent.isNotBlank()) {
                        // Здесь можно добавить диалог "Сохранить изменения?"
                        // Пока просто возвращаемся назад
                    }
                    viewModel.cancelEdit()
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