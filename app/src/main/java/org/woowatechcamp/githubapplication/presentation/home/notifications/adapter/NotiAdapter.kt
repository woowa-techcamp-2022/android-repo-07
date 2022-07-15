package org.woowatechcamp.githubapplication.presentation.home.notifications.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.data.notifications.model.NotiResponseItem
import org.woowatechcamp.githubapplication.databinding.ItemNotiBinding
import org.woowatechcamp.githubapplication.util.ItemDiffCallback
import java.net.URL

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
            binding.noti = item.getModel()
            binding.apply {
                CoroutineScope(Dispatchers.IO).launch {
                    val inputStream = URL(item.repository.owner.avatar_url).openStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    withContext(Dispatchers.Main) {
                        ivNoti.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}