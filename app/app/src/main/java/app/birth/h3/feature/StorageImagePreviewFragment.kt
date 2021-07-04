package app.birth.h3.feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import app.birth.h3.R
import app.birth.h3.databinding.FragmentStorageImagePreviewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StorageImagePreviewFragment : Fragment(R.layout.fragment_storage_image_preview) {
    private val viewModel: StorageImagePreviewViewModel by viewModels()
    private var _binding: FragmentStorageImagePreviewBinding? = null
    private val binding get() = _binding!!
    private val args: StorageImagePreviewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStorageImagePreviewBinding.bind(view)

        viewModel.loadImage(args.imageId)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
