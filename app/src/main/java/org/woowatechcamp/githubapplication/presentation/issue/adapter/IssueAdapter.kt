package org.woowatechcamp.githubapplication.presentation.issue.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.data.issue.model.IssueResponseItem
import org.woowatechcamp.githubapplication.databinding.ItemIssueBinding
import org.woowatechcamp.githubapplication.presentation.view_util.ItemDiffCallback
import org.woowatechcamp.githubapplication.util.DateUtil

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
            binding.apply {
                when (item.state) {
                    "open" -> { ivIssue.setImageResource(R.drawable.ic_issue_open_24) }
                    "closed" -> { ivIssue.setImageResource(R.drawable.ic_issue_closed_24) }
                    "all" -> { ivIssue.setImageResource(R.drawable.ic_issue_open_24) }
                }
                // full_name 으로 하면 너무 길어져서 일단 name 으로 함
//                tvIssueName.text = item.repository.full_name
                tvIssueName.text = item.repository.name
                tvIssueNum.text = "#${item.number}"
                tvIssueTitle.text = item.title
                tvIssueDate.text = DateUtil.getTimeDiff(item.updated_at)
            }

        }
    }
}