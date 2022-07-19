package org.woowatechcamp.githubapplication.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewpagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Pair<Fragment, String>>()

    fun addFragment(fm: Fragment, name: String) {
        fragmentList.add(Pair(fm, name))
    }

    fun getTitle(position: Int): String {
        return fragmentList[position].second
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position].first
    }
}