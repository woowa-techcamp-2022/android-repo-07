package org.woowatechcamp.githubapplication.presentation.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.woowatechcamp.githubapplication.data.notifications.model.NotiResponseItem
import org.woowatechcamp.githubapplication.databinding.ItemNotiBinding
import org.woowatechcamp.githubapplication.presentation.view_util.ItemDiffCallback
import org.woowatechcamp.githubapplication.util.DateUtil

class NotiAdapter : ListAdapter<NotiResponseItem, NotiAdapter.NotiViewHolder>(
    ItemDiffCallback<NotiResponseItem>(
        onContentsTheSame = { old, new -> old == new },
        onItemsTheSame = { old, new -> old.id == new.id }
    )
) {
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotiBinding.inflate(inflater, parent, false)
        return NotiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotiViewHolder(private val binding : ItemNotiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : NotiResponseItem) {
            binding.apply {
                tvNotiName.text = item.repository.name
                tvNotiTitle.text = item.subject.title
                tvNotiDate.text = DateUtil.getTimeDiff(item.updated_at)
                ivNoti
                // full_name 으로 하면 너무 길어져서 일단 name 으로 함
//                tvIssueName.text = item.repository.name
//                tvIssueNum.text = "#${item.number}"
//                tvIssueTitle.text = item.title
//                tvIssueDate.text = DateUtil.getTimeDiff(item.updated_at)
                // 이미지 변경해주기
            }
        }
    }
}