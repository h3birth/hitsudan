package app.birth.h3.feature

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.recyclerview.widget.GridLayoutManager
import app.birth.h3.R
import app.birth.h3.databinding.FragmentStorageImageListBinding
import app.birth.h3.local.entity.StorageImages
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class StorageImageListFragment : Fragment(R.layout.fragment_storage_image_list) {
    private val viewModel: StorageImageListViewModel by viewModels()
    private var _binding: FragmentStorageImageListBinding? = null
    private val binding get() = _binding
    val pagingAdapter = StorageImagesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentStorageImageListBinding.bind(view)
        binding?.toolbar?.setNavigationOnClickListener {
            activity?.finish()
        }

        binding?.recyclerView?.apply {
            adapter = pagingAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        viewModel.init(requireContext())
        viewModel.loadImage()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
