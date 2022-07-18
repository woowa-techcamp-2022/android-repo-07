package org.woowatechcamp.githubapplication.presentation.home.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.FragmentNotificationsBinding
import org.woowatechcamp.githubapplication.presentation.home.notifications.adapter.NotiAdapter
import org.woowatechcamp.githubapplication.presentation.home.notifications.model.NotiModel
import org.woowatechcamp.githubapplication.util.ItemDecorationUtil
import org.woowatechcamp.githubapplication.util.SwipeCallback
import org.woowatechcamp.githubapplication.util.SwipeListener
import org.woowatechcamp.githubapplication.util.ext.getDeliNumber

@AndroidEntryPoint
class NotificationsFragment : Fragment(), SwipeListener, SwipeRefreshLayout.OnRefreshListener {

    private var _binding : FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notiAdapter : NotiAdapter
    private lateinit var swipeCallback: SwipeCallback

    private val mVieModel by viewModels<NotiViewModel>()

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

        binding.swipeNoti.setOnRefreshListener(this)
        mVieModel.getNoti()
    }

    private fun initAdapter() {
        notiAdapter = NotiAdapter()
        binding.rvNoti.apply {
            adapter = notiAdapter
            addItemDecoration(ItemDecorationUtil.ItemDividerDecoration(1f, 0f, requireActivity().getColor(R.color.navy)))
        }
        swipeCallback = SwipeCallback(0, ItemTouchHelper.LEFT, requireActivity())
        swipeCallback.setListener(this)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvNoti)
    }

    private fun observeData() {
        // 현재 아예 아무 응답도 안 와서 성공해도 오류 메시지가 뜸
        mVieModel.markMessage.observe(viewLifecycleOwner) {
//            showSnackBar(binding.root, it, requireActivity())
        }
        mVieModel.notiList.observe(viewLifecycleOwner) {
            notiAdapter.submitList(it)
            binding.swipeNoti.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun swipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val list = ArrayList<NotiModel>()
        list.addAll(notiAdapter.currentList)
        val item = list[position]
        list.removeAt(position)
        notiAdapter.submitList(list.toList())
        // thread ID 값을 가져와서 알림 읽음 설정한다.
        val threadNum = item.url.getDeliNumber("threads/")
        if (threadNum > 0) {
            mVieModel.markNoti(threadNum)
        }
    }

    override fun onRefresh() {
        mVieModel.getNoti()
    }
}