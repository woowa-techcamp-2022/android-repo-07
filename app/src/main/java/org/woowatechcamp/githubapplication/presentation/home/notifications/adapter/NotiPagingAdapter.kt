package org.woowatechcamp.githubapplication.presentation.home.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import org.woowatechcamp.githubapplication.databinding.ItemNotiBinding
import org.woowatechcamp.githubapplication.domain.entity.NotiModel
import org.woowatechcamp.githubapplication.util.ItemDiffCallback

class NotiPagingAdapter : PagingDataAdapter<NotiModel, NotiAdapter.NotiViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.id == new.id }
    )
) {
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiAdapter.NotiViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val binding = ItemNotiBinding.inflate(inflater, parent, false)
        return NotiAdapter.NotiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotiAdapter.NotiViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}