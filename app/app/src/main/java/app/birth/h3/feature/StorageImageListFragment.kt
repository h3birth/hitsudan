package app.birth.h3.feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.birth.h3.R
import app.birth.h3.databinding.FragmentStorageImageListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageImageListFragment : Fragment(R.layout.fragment_storage_image_list) {
    private val viewModel: StorageImageListViewModel by viewModels()
    private var _binding: FragmentStorageImageListBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentStorageImageListBinding.bind(view)
        binding?.toolbar?.setNavigationOnClickListener {
            activity?.finish()
        }

        viewModel.init(requireContext())
        viewModel.loadImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
