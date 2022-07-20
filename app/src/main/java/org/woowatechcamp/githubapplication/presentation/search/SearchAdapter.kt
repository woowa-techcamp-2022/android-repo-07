package org.woowatechcamp.githubapplication.presentation.search

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.woowatechcamp.githubapplication.databinding.ItemSearchBinding
import org.woowatechcamp.githubapplication.domain.entity.SearchInfo
import org.woowatechcamp.githubapplication.util.ItemDiffCallback
import java.net.URL

class SearchAdapter : PagingDataAdapter<SearchInfo, SearchAdapter.SearchViewHolder>(
    ItemDiffCallback(
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
                runCatching {
                    val mInputStream = URL(data.profileImg).openStream()
                    BitmapFactory.decodeStream(mInputStream)
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        binding.ivProfile.setImageBitmap(it)
                        cancel()
                    }
                }.onFailure {
                    // Todo : 서치 뷰홀더 에러 잡기
                    cancel()
                }
            }
        }
    }
}
