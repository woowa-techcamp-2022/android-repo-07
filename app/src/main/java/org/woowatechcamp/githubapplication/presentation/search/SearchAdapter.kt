package org.woowatechcamp.githubapplication.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.woowatechcamp.githubapplication.databinding.ItemSearchBinding
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import org.woowatechcamp.githubapplication.util.ItemDiffCallback

class SearchAdapter : ListAdapter<SearchInfo, SearchAdapter.SearchViewHolder>(
    ItemDiffCallback<SearchInfo>(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.id == new.id }
    )
) {
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val binding = ItemSearchBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: SearchInfo) {
            binding.data = data
        }
    }
}
