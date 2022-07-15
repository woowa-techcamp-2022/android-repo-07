package org.woowatechcamp.githubapplication.presentation.home.issue.adapter

import android.content.Context
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.data.issue.IssueCategory

class IssueSpinAdapter(
    context : Context,
    resource : Int,
    items : List<IssueCategory>
) : ArrayAdapter<IssueCategory>(context, resource, items) {

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private val rotateForward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
    private val rotateBackward = AnimationUtils.loadAnimation(context, R.anim.rotate_backward)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.item_spin_issue_header, parent, false)

        setItem(view, IssueCategory("Option", true))
        val listener = ViewTreeObserver.OnWindowFocusChangeListener { p0 ->
            if (p0) {
                val ivIssueIcon = view.findViewById<ImageView>(R.id.iv_issue_icon)
                ivIssueIcon.startAnimation(rotateBackward)
            }
            // 지금 포커스가 생긴 상황
            else {
                val ivIssueIcon = view.findViewById<ImageView>(R.id.iv_issue_icon)
                ivIssueIcon.startAnimation(rotateForward)
            }
        }
        view.viewTreeObserver.addOnWindowFocusChangeListener(listener)
//        view.viewTreeObserver.removeOnWindowFocusChangeListener(listener)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.item_spin_issue, parent, false)
        getItem(position)?.let { item ->
            setItem(view, item)
        }
        return view
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