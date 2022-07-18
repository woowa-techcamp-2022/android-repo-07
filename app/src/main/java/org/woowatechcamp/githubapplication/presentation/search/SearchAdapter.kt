package org.woowatechcamp.githubapplication.presentation.search

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.woowatechcamp.githubapplication.databinding.ItemSearchBinding
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import org.woowatechcamp.githubapplication.util.ItemDiffCallback
import org.woowatechcamp.githubapplication.util.showSnackBar
import java.net.URL

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
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: SearchInfo) {
            binding.data = data
            CoroutineScope(Dispatchers.IO).launch {
                kotlin.runCatching {
                    val mInputStream = URL(data.profileImg).openStream()
                    BitmapFactory.decodeStream(mInputStream)
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        binding.ivProfile.setImageBitmap(it)
                    }
                }.onFailure {

                }
            }
        }
    }
}
