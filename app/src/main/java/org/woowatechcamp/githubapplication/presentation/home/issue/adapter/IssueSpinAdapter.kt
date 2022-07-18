package org.woowatechcamp.githubapplication.presentation.home.issue.adapter

import android.animation.AnimatorInflater
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.data.issue.IssueCategory

class IssueSpinAdapter(
    context : Context,
    resource : Int,
    items: List<IssueCategory>
) : ArrayAdapter<IssueCategory>(context, resource, items) {

    private lateinit var inflater : LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        initInflater(parent)

        val view = convertView ?: inflater.inflate(R.layout.item_spin_issue_header, parent, false)

        val ivIssueIcon = view.findViewById<ImageView>(R.id.iv_issue_icon)
        ivIssueIcon.stateListAnimator = AnimatorInflater.loadStateListAnimator(
            view.context, R.xml.anim_spin_icon)

        setItem(view, IssueCategory("Option", true))
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        initInflater(parent)

        val view = convertView ?: inflater.inflate(R.layout.item_spin_issue, parent, false)
        getItem(position)?.let { item ->
            setItem(view, item)
        }
        return view
    }

    private fun initInflater(parent: ViewGroup) {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)
    }

    private fun setItem(view: View, item : IssueCategory) {
        val tvIssueTitle = view.findViewById<TextView>(R.id.tv_issue_title)
        val ivIssueIcon = view.findViewById<ImageView>(R.id.iv_issue_icon)
        tvIssueTitle.text = item.title
        if (item.selected) {
            ivIssueIcon.visibility = View.VISIBLE
            tvIssueTitle.setTextColor(view.context.getColor(R.color.white))
        } else {
            ivIssueIcon.visibility = View.INVISIBLE
            tvIssueTitle.setTextColor(view.context.getColor(R.color.grey))
        }
    }
}