package org.woowatechcamp.githubapplication.presentation.search

import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivitySearchBinding
import org.woowatechcamp.githubapplication.util.ItemDecorationUtil
import org.woowatechcamp.githubapplication.util.ResolutionMetrics
import org.woowatechcamp.githubapplication.util.ext.closeKeyboard
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    // TODO : Inject 로직 수정 필요
    @Inject
    lateinit var resolutionMetrics: ResolutionMetrics
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    private val searchAdapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        initAdapter()
        observeData()
    }

    private fun observeData() {
        viewModel.searchUiState.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is SearchViewModel.SearchUiState.Success -> {
                        isRepoNotEmpty(true)
                        searchAdapter.submitList(it.data)
                    }
                    is SearchViewModel.SearchUiState.Empty -> {
                        isRepoNotEmpty(false)
                    }
                    is SearchViewModel.SearchUiState.Loading -> {
                        // TODO : Loading, Error 추후 구현
                    }
                    else -> {
                        // TODO : Loading, Error 추후 구현
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun isRepoNotEmpty(isNotEmpty: Boolean) {
        binding.isNotEmpty = isNotEmpty
    }

    private fun initAdapter() {
        with(resolutionMetrics) {
            binding.rvSearch.apply {
                adapter = searchAdapter
                addItemDecoration(
                    ItemDecorationUtil.ItemDividerDecoration(
                        toPixel(1),
                        toPixel(24),
                        getColor(R.color.navy)
                    )
                )
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus

        if (view != null && ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val locationList = IntArray(2)
            view.getLocationOnScreen(locationList)
            val x = ev.rawX + view.left - locationList[0]
            val y = ev.rawY + view.top - locationList[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                closeKeyboard(view)
                view.clearFocus()
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}
