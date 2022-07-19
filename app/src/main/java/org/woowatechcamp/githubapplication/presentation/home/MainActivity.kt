package org.woowatechcamp.githubapplication.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivityMainBinding
import org.woowatechcamp.githubapplication.presentation.adapter.ViewpagerAdapter
import org.woowatechcamp.githubapplication.presentation.home.issue.IssueFragment
import org.woowatechcamp.githubapplication.presentation.home.notifications.NotificationsFragment
import org.woowatechcamp.githubapplication.presentation.profile.ProfileActivity
import org.woowatechcamp.githubapplication.presentation.search.SearchActivity
import org.woowatechcamp.githubapplication.util.ext.getRoundDrawable
import org.woowatechcamp.githubapplication.util.ext.setBitmapWithCoil
import org.woowatechcamp.githubapplication.util.ext.startActivity
import org.woowatechcamp.githubapplication.util.onError
import org.woowatechcamp.githubapplication.util.onSuccess
import org.woowatechcamp.githubapplication.util.showSnackBar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewpagerAdapter: ViewpagerAdapter
    private lateinit var menu: Menu
    private val issueFragment: IssueFragment by lazy { IssueFragment() }
    private val notificationsFragment: NotificationsFragment by lazy { NotificationsFragment() }

    private val viewModel by viewModels<MainViewModel>()

    private var profileIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        initAdapter()
        observeData()

        if (savedInstanceState == null) {
            viewModel.getUser()
        }
    }

    private fun initAdapter() {
        viewpagerAdapter = ViewpagerAdapter(this)
        viewpagerAdapter.addFragment(issueFragment, getString(R.string.main_tab_title_issue))
        viewpagerAdapter.addFragment(
            notificationsFragment,
            getString(R.string.main_tab_title_notifications)
        )
        binding.vpMain.apply {
            adapter = viewpagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = false
        }
        TabLayoutMediator(binding.tlMain, binding.vpMain) { tab, position ->
            tab.text = viewpagerAdapter.getTitle(position)
        }.attach()
    }

    private fun observeData() {
        viewModel.mainState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                with(state) {
                    onSuccess {
                        CoroutineScope(Dispatchers.IO).launch {
                            it.imgUrl.setBitmapWithCoil(this@MainActivity) { bitmap ->
                                menu.getItem(1).icon = bitmap.getRoundDrawable(resources)
                            }
                        }
                        profileIntent = Intent(this@MainActivity, ProfileActivity::class.java)
                            .putExtra(getString(R.string.profile_item), it)
                    }
                    onError {
                        showSnackBar(binding.root, it, this@MainActivity)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_search -> {
                startActivity<SearchActivity>()
            }
            R.id.menu_main_profile -> {
                if (profileIntent == null) {
                    showSnackBar(binding.root, "데이터를 불러오는 데 실패했습니다.", this@MainActivity)
                } else {
                    startActivity(profileIntent)
                }
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}