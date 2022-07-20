package org.woowatechcamp.githubapplication.presentation.home.issue.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import org.woowatechcamp.githubapplication.databinding.ItemIssueBinding
import org.woowatechcamp.githubapplication.databinding.ItemSearchBinding
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import org.woowatechcamp.githubapplication.presentation.home.issue.adapter.IssueAdapter
import org.woowatechcamp.githubapplication.presentation.home.issue.model.IssueModel
import org.woowatechcamp.githubapplication.util.ItemDiffCallback


class IssuePagingAdapter : PagingDataAdapter<IssueModel, IssueAdapter.IssueViewHolder>(
    ItemDiffCallback(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.id == new.id }
    )
) {
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueAdapter.IssueViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val binding = ItemIssueBinding.inflate(inflater, parent, false)
        return IssueAdapter.IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueAdapter.IssueViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}
