package org.woowatechcamp.githubapplication.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.ActivityMainBinding
import org.woowatechcamp.githubapplication.presentation.adapter.ViewpagerAdapter
import org.woowatechcamp.githubapplication.presentation.issue.IssueFragment
import org.woowatechcamp.githubapplication.presentation.notifications.NotificationsFragment
import org.woowatechcamp.githubapplication.presentation.profile.ProfileActivity
import org.woowatechcamp.githubapplication.presentation.search.SearchActivity
import org.woowatechcamp.githubapplication.util.showSnackBar
import kotlin.math.max

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewpagerAdapter : ViewpagerAdapter
    private lateinit var menu : Menu
    private val issueFragment : IssueFragment by lazy { IssueFragment() }
    private val notificationsFragment: NotificationsFragment by lazy { NotificationsFragment() }

    private val mViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        initAdapter()
        observeData()

        mViewModel.getUser()
    }

    private fun initAdapter() {
        viewpagerAdapter = ViewpagerAdapter(this)
        viewpagerAdapter.addFragment(issueFragment, getString(R.string.main_tab_title_issue))
        viewpagerAdapter.addFragment(notificationsFragment, getString(R.string.main_tab_title_notifications))
        binding.vpMain.apply {
            adapter = viewpagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = true // 사용자가 직접 swipe 할 수 있도록 하는 것
        }
        TabLayoutMediator(binding.tlMain, binding.vpMain) { tab, position ->
            tab.text = viewpagerAdapter.getTitle(position)
        }.attach()
    }

    private fun observeData() {
        mViewModel.errorMessage.observe(this) {
            showSnackBar(binding.root, it, this)
        }
        mViewModel.userProfile.observe(this) {
            val round = RoundedBitmapDrawableFactory.create(resources, it)
            round.cornerRadius = max(it.width, it.height) / 2f
            menu.getItem(1).icon = round
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
            R.id.menu_main_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("profile_item", mViewModel.userInfo.value)
                startActivity(intent)
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }}
