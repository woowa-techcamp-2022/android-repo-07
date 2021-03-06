package org.woowatechcamp.githubapplication.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
        binding.toolbarSearch.setNavigationOnClickListener {
            finish()
        }
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initTextChangeEvent()
        initAdapter()
        observeData()
    }

    private fun observeData() {
        viewModel.searchUiState.flowWithLifecycle(lifecycle)
            .onEach {
                when (it) {
                    is SearchViewModel.SearchUiState.Success -> {
                        isRepoNotEmpty(true)
                    }
                    is SearchViewModel.SearchUiState.Empty -> {
                        isRepoNotEmpty(false)
                    }
                    else -> {
                        Log.d("422에러 : ","에러가 발생했습니다")
                        getRepoSearch(binding.etSearch.text.toString())
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun getRepoSearch(keyword : String) {
        viewModel.getRepoSearch(keyword).flowWithLifecycle(lifecycle)
            .onEach {
                searchAdapter.submitData(it)
            }
            .launchIn(lifecycleScope)
    }

    private fun initTextChangeEvent() {
        binding.etSearch.setTextChangeListener {
            viewModel.setLoading()
            lifecycleScope.launch {
                delay(150L)
                getRepoSearch(binding.etSearch.text.toString())
            }
            // viewModel.textChangeAction.invoke(binding.etSearch.text.toString())
            isRepoNotEmpty(it)
        }

        binding.etSearch.setDeleteClickListener {
            viewModel.setEmpty()
        }
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
