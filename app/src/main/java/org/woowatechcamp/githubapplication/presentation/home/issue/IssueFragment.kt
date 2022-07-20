package org.woowatechcamp.githubapplication.presentation.home.issue

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.data.issue.IssueCategory
import org.woowatechcamp.githubapplication.databinding.FragmentIssueBinding
import org.woowatechcamp.githubapplication.presentation.home.issue.adapter.IssueAdapter
import org.woowatechcamp.githubapplication.presentation.home.issue.adapter.IssueSpinAdapter
import org.woowatechcamp.githubapplication.util.*
import javax.inject.Inject
import kotlin.onSuccess

@AndroidEntryPoint
class IssueFragment : Fragment() {

    private var _binding: FragmentIssueBinding? = null
    private val binding get() = _binding!!
    private lateinit var issueAdapter: IssueAdapter
    private var option = "open"

    private val viewModel by viewModels<IssueViewModel>()

    @Inject
    lateinit var metrics: ResolutionMetrics

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIssueBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeData()

        binding.swipeIssue.setOnRefreshListener { viewModel.getIssues(option) }
    }

    private fun initAdapter() {
        issueAdapter = IssueAdapter()
        binding.rvIssue.apply {
            adapter = issueAdapter
            addItemDecoration(
                ItemDecorationUtil.ItemDividerDecoration(
                    metrics.toPixel(1), 0f, requireActivity().getColor(R.color.navy)
                )
            )
        }
        val spinItems = listOf(
            IssueCategory(getString(R.string.open_category), false),
            IssueCategory(getString(R.string.closed_category), false),
            IssueCategory(getString(R.string.all_category), false)
        )

        val spinAdapter = IssueSpinAdapter(requireActivity(), 0, spinItems)

        binding.spinIssue.apply {
            adapter = spinAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    p1?.let {
                        option = when (p2) {
                            STATE_CLOSED -> {
                                getString(R.string.closed)
                            }
                            STATE_ALL -> {
                                getString(R.string.all)
                            }
                            else -> {
                                getString(R.string.open)
                            }
                        }
                        for (i in spinItems.indices) {
                            spinItems[i].selected = (i == p2)
                        }
                        viewModel.getIssues(option)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun observeData() {
        viewModel.issueState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                with(state) {
                    onSuccess {
                        issueAdapter.submitList(it)
                        binding.swipeIssue.isRefreshing = false
                    }
                    onError {
                        showSnackBar(binding.root, it, requireActivity())
                        binding.swipeIssue.isRefreshing = false
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val STATE_OPEN = 0
        private const val STATE_CLOSED = 1
        private const val STATE_ALL = 2
    }
}