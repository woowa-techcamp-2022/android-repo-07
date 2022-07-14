package org.woowatechcamp.githubapplication.presentation.issue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.presentation.MainViewModel
import org.woowatechcamp.githubapplication.databinding.FragmentIssueBinding
import org.woowatechcamp.githubapplication.presentation.issue.adapter.IssueAdapter
import org.woowatechcamp.githubapplication.presentation.issue.adapter.IssueSpinAdapter
import org.woowatechcamp.githubapplication.presentation.view_util.CustomItemDivider
import org.woowatechcamp.githubapplication.util.showSnackBar

@AndroidEntryPoint
class IssueFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding : FragmentIssueBinding? = null
    private val binding get() = _binding!!
    private lateinit var issueAdapter : IssueAdapter

    private val mViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIssueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        issueAdapter = IssueAdapter()

        binding.rvIssue.apply {
            adapter = issueAdapter
            activity?.let {
                addItemDecoration(CustomItemDivider(1f, it.getColor(R.color.navy)))
            }
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }

        val spinAdapter = IssueSpinAdapter(requireActivity())

        binding.llIssueSpin.apply {
            adapter = spinAdapter
            onItemSelectedListener = this@IssueFragment
        }

        mViewModel.errorMessage.observe(viewLifecycleOwner) {
            showSnackBar(binding.root, it, requireActivity())
        }

        mViewModel.issueList.observe(viewLifecycleOwner) {
            issueAdapter.submitList(it)
        }
        mViewModel.getIssues("all")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        p0?.let {
            when (p2) {
                0 -> {
                    val list = mViewModel.issueList.value
                    list?.let {
                        issueAdapter.submitList(list.filter { it.state == "open" }.toList())
                    }
                }
                1 -> {
                    val list = mViewModel.issueList.value
                    list?.let {
                        issueAdapter.submitList(list.filter { it.state == "closed" }.toList())
                    }
                }
                2 -> {
                    val list = mViewModel.issueList.value
                    list?.let {
                        issueAdapter.submitList(list.filter { it.state == "all" }.toList())
                    }
                }
                else -> {
                    val list = mViewModel.issueList.value
                    list?.let {
                        issueAdapter.submitList(list.filter { it.state == "open" }.toList())
                    }

                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}