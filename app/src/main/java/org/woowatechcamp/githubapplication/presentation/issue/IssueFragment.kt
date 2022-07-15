package org.woowatechcamp.githubapplication.presentation.issue

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.data.issue.IssueCategory
import org.woowatechcamp.githubapplication.presentation.MainViewModel
import org.woowatechcamp.githubapplication.databinding.FragmentIssueBinding
import org.woowatechcamp.githubapplication.presentation.issue.adapter.IssueAdapter
import org.woowatechcamp.githubapplication.presentation.issue.adapter.IssueSpinAdapter
import org.woowatechcamp.githubapplication.presentation.view_util.CustomItemDivider
import org.woowatechcamp.githubapplication.util.showSnackBar

@AndroidEntryPoint
class IssueFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding : FragmentIssueBinding? = null
    private val binding get() = _binding!!
    private lateinit var issueAdapter : IssueAdapter
    private var option = "open"

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

        binding.swipeIssue.setOnRefreshListener(this)

//        mViewModel.getIssues(option)
    }

    private fun initAdapter() {
        issueAdapter = IssueAdapter()
        binding.rvIssue.apply {
            adapter = issueAdapter
            addItemDecoration(CustomItemDivider(1f, requireActivity().getColor(R.color.navy)))
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }

        val spinItems = listOf(
            IssueCategory("Open", true),
            IssueCategory("Closed", false),
            IssueCategory("All", false)
        )
        val spinAdapter = IssueSpinAdapter(requireActivity(), 0, spinItems)

        binding.llIssueSpin.apply {
            adapter = spinAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    p0?.let {
                        option = when (p2) {
                            0 -> { "open" }
                            1 -> { "closed" }
                            2 -> { "all" }
                            else -> { "open" }
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
            if (option == "all") {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HELLO", "destory")
    }

}