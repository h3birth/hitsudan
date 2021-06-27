package app.birth.h3.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.birth.h3.databinding.ItemStorageImageBinding
import app.birth.h3.local.entity.StorageImages

class StorageImagesAdapter: PagingDataAdapter<StorageImages, StorageImagesAdapter.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<StorageImages>() {
            override fun areItemsTheSame(oldItem: StorageImages, newItem: StorageImages): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StorageImages, newItem: StorageImages): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.storageImage = getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStorageImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: ItemStorageImageBinding) : RecyclerView.ViewHolder(binding.root)
}
