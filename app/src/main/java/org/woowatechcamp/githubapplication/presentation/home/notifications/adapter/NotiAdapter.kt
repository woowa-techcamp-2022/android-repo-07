package org.woowatechcamp.githubapplication.presentation.home.notifications.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.databinding.ItemNotiBinding
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.ItemDiffCallback
import java.net.URL

class NotiAdapter : ListAdapter<NotiModel, NotiAdapter.NotiViewHolder>(
    ItemDiffCallback<NotiModel>(
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
        fun bind(item : NotiModel) {
            binding.noti = item
            binding.apply {
                Glide.with(binding.root)
                    .load(item.imgUrl)
                    .centerCrop()
                    .into(binding.ivNoti)
            }
        }
    }
}