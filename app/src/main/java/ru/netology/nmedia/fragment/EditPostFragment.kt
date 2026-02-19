package ru.netology.nmedia.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
class EditPostFragment : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.editedPost.observe(viewLifecycleOwner) { post ->
            post?.let {
                binding.editContent.setText(it.content)
            }
        }
        binding.saveButton.setOnClickListener {
            val content = binding.editContent.text.toString().trim()

            if (content.isBlank()) {
                Toast.makeText(requireContext(), "Текст не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.changeContent(content)
            viewModel.save()

            parentFragmentManager.popBackStack()
        }
        // УДАЛИТЕ ЭТИ СТРОКИ (если кнопки нет в layout)
        // binding.cancelButton?.setOnClickListener {
        //     parentFragmentManager.popBackStack()
        // }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}