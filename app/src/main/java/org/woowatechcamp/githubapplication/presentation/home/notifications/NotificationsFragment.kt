package org.woowatechcamp.githubapplication.presentation.home.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.FragmentNotificationsBinding
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.presentation.home.notifications.paging.NotiPagingAdapter
import org.woowatechcamp.githubapplication.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val notiPagingAdapter by lazy { NotiPagingAdapter() }

    private val refreshItems = mutableListOf<NotiModel>()

    private val viewModel by viewModels<NotiViewModel>()

    @Inject
    lateinit var metrics: ResolutionMetrics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeData()

        if (savedInstanceState == null) {
            getNotiPaging()
        }
    }

    private fun initAdapter() {
        binding.rvNoti.apply {
            adapter = notiPagingAdapter
//            itemAnimator = null
            addItemDecoration(
                ItemDecorationUtil.ItemDividerDecoration(
                    metrics.toPixel(1), 0f, requireActivity().getColor(R.color.navy)
                )
            )
        }

        val swipeCallback = SwipeCallback(
            0,
            ItemTouchHelper.LEFT,
            requireActivity()
        ) { position ->
            with(notiPagingAdapter.snapshot().items[position]) {
                viewModel.removeItem(this)
                threadId?.let { viewModel.markNoti(it) }
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvNoti)

        binding.swipeNoti.setOnRefreshListener {
            notiPagingAdapter.refresh()
            binding.swipeNoti.isRefreshing = false
        }

        notiPagingAdapter.addOnPagesUpdatedListener {
            CoroutineScope(Dispatchers.IO).launch {
                with(notiPagingAdapter.snapshot().items
                    .filter { noti -> noti !in refreshItems }
                ) {
                    for (noti in this@with) {
                        viewModel.getComments(noti)
                    }
                    refreshItems.addAll(this@with)
                }
                cancel()
            }
        }
    }

    private fun observeData() {
        viewModel.notiCommentState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                with(state) {
                    onSuccess {
                        val list = notiPagingAdapter.snapshot().items
                        with(list) {
                            find { noti -> noti.id == it.id }?.let { found ->
                                found.commentNum = it.commentNum
                                notiPagingAdapter.notifyItemChanged(indexOf(found))
                            }
                        }
                    }
                    onError {
                        showSnackBar(binding.root, it, requireActivity())
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.markState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                with(state) {
                    onSuccess {
                        showSnackBar(binding.root, it, requireActivity())
                    }
                    onError {
                        showSnackBar(binding.root, it, requireActivity())
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun getNotiPaging() {
        viewModel.getNotiPaging(notiPagingAdapter)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}