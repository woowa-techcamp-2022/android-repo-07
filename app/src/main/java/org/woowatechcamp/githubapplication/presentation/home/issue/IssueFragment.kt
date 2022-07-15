package org.woowatechcamp.githubapplication.presentation.home.issue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.data.issue.IssueCategory
import org.woowatechcamp.githubapplication.databinding.FragmentIssueBinding
import org.woowatechcamp.githubapplication.presentation.home.issue.adapter.IssueAdapter
import org.woowatechcamp.githubapplication.presentation.home.issue.adapter.IssueSpinAdapter
import org.woowatechcamp.githubapplication.util.ItemDecorationUtil
import org.woowatechcamp.githubapplication.util.showSnackBar

@AndroidEntryPoint
class IssueFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding : FragmentIssueBinding? = null
    private val binding get() = _binding!!
    private lateinit var issueAdapter : IssueAdapter
    private var option = "open"

    private var viewListener : ViewTreeObserver.OnWindowFocusChangeListener? = null

    private val mViewModel by viewModels<IssueViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIssueBinding.inflate(inflater, container, false)
        Log.d("HELLO", "craet view")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HELLO", "view created")

        initAdapter()
        observeData()

        viewListener = ViewTreeObserver.OnWindowFocusChangeListener { hasFocus ->
            if (hasFocus) {
                binding.llIssueContainer.setBackgroundResource(R.drawable.rectangle_navy_radius_14)
            }
            // 지금 포커스가 생긴 상황
            else {
                binding.llIssueContainer.setBackgroundResource(R.drawable.bg_spin_issue)
            }
        }

        binding.swipeIssue.setOnRefreshListener(this)
    }

    private fun initAdapter() {
        issueAdapter = IssueAdapter()
        binding.rvIssue.apply {
            adapter = issueAdapter
            addItemDecoration(ItemDecorationUtil.ItemDividerDecoration(1f, 0f, requireActivity().getColor(R.color.navy)))
        }
        
        val spinItems = listOf(
            IssueCategory(getString(R.string.open_category), true),
            IssueCategory(getString(R.string.closed_category), false),
            IssueCategory(getString(R.string.all_category), false)
        )
        val spinAdapter = IssueSpinAdapter(requireActivity(), 0, spinItems)

        binding.llIssueSpin.apply {
            adapter = spinAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    p0?.let {
                        option = when (p2) {
                            0 -> { getString(R.string.open) }
                            1 -> { getString(R.string.closed) }
                            2 -> { getString(R.string.all) }
                            else -> { getString(R.string.open) }
                        }
                        mViewModel.getIssues(option)
                        for (i in spinItems.indices) {
                            spinItems[i].selected = (i == p2)
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        }
    }

    private fun observeData() {
        mViewModel.errorMessage.observe(viewLifecycleOwner) {
            showSnackBar(binding.root, it, requireActivity())
        }

        mViewModel.issueList.observe(viewLifecycleOwner) {
            if (option == getString(R.string.all)) {
                issueAdapter.submitList(it)
            } else {
                issueAdapter.submitList(it.filter { item -> item.state == option }.toList())
            }
            binding.swipeIssue.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        Log.d("HELLO", "destory view")
    }

    override fun onRefresh() {
        mViewModel.getIssues(option)
    }

    override fun onStart() {
        super.onStart()
        Log.d("HELLO", "started")

        binding.llIssueSpin.viewTreeObserver.addOnWindowFocusChangeListener(viewListener)
    }

    override fun onResume() {
        super.onResume()
        Log.d("HELLO", "reseumsed")

    }

    override fun onPause() {
        super.onPause()
        Log.d("HELLO", "started")
    }

    override fun onStop() {
        super.onStop()
        Log.d("HELLO", "stoped")

        viewListener?.let {
            binding.llIssueSpin.viewTreeObserver.removeOnWindowFocusChangeListener(it)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HELLO", "destory")
    }

}