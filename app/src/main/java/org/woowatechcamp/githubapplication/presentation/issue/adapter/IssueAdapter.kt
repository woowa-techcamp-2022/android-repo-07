package org.woowatechcamp.githubapplication.presentation.issue.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.woowatechcamp.githubapplication.data.issue.model.IssueResponseItem
import org.woowatechcamp.githubapplication.databinding.ItemIssueBinding
import org.woowatechcamp.githubapplication.presentation.view_util.ItemDiffCallback

class IssueAdapter : ListAdapter<IssueResponseItem, IssueAdapter.IssueViewHolder>(
    ItemDiffCallback<IssueResponseItem>(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.id == new.id }
    )
) {
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        val binding = ItemIssueBinding.inflate(inflater, parent, false)
        return IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class IssueViewHolder(private val binding : ItemIssueBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : IssueResponseItem) {
            binding.issue = item.getModel()
        }
    }
}