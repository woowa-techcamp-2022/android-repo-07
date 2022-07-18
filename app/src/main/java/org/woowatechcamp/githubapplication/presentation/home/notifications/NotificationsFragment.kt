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
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.FragmentNotificationsBinding
import org.woowatechcamp.githubapplication.presentation.home.notifications.adapter.NotiAdapter
import org.woowatechcamp.githubapplication.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding : FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notiAdapter : NotiAdapter
    private lateinit var swipeCallback: SwipeCallback
    private lateinit var swipeListener : SwipeListener

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

        if (savedInstanceState == null) { viewModel.getNoti() }
    }

    private fun initAdapter() {
        notiAdapter = NotiAdapter()
        binding.rvNoti.apply {
            adapter = notiAdapter
            addItemDecoration(ItemDecorationUtil.ItemDividerDecoration(
                metrics.toPixel(1), 0f, requireActivity().getColor(R.color.navy)))
        }

        swipeListener = object : SwipeListener {
            override fun swipeItem(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int,
                position: Int
            ) {
                val list = notiAdapter.currentList.toMutableList()
                val item = list[position]
                list.removeAt(position)
                notiAdapter.submitList(list)

                item.threadId?.let { viewModel.markNoti(it) }
            }
        }

        swipeCallback = SwipeCallback(0, ItemTouchHelper.LEFT, requireActivity())
        swipeCallback.setListener(swipeListener)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvNoti)

        binding.swipeNoti.setOnRefreshListener { viewModel.getNoti() }
    }

    private fun observeData() {
        viewModel.notiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        notiAdapter.submitList(it.value)
                        binding.swipeNoti.isRefreshing = false }
                    is UiState.Error -> {
                        showSnackBar(binding.root, it.msg, requireActivity())
                        binding.swipeNoti.isRefreshing = false }
                    else -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.notiCommentState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        val item = it.value
                        with(notiAdapter.currentList) {
                            find { noti -> noti.id == item.id }?.let { found ->
                                found.commentNum = item.commentNum
                                notiAdapter.notifyItemChanged(indexOf(found))
                            }
                        }
                    }
                    is UiState.Error -> {
                        showSnackBar(binding.root, it.msg, requireActivity()) }
                    else -> {}
                }
            }.launchIn(lifecycleScope)

        viewModel.markState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                when (it) {
                    is UiState.Success -> {
                        showSnackBar(binding.root, it.value, requireActivity()) }
                    is UiState.Error -> {
                        showSnackBar(binding.root, it.msg, requireActivity()) }
                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}