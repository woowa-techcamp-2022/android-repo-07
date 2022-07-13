package org.woowatechcamp.githubapplication.presentation.issue.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.data.issue.IssueCategory

class IssueSpinAdapter(
    context : Context
) : ArrayAdapter<IssueCategory>(context, 0, IssueCategory.values()) {

    private val inflater : LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.layout_issue_spin, parent, false)
        val ivIssueContainer = view.findViewById<ImageView>(R.id.iv_issue_container)
//        ivIssueContainer.animate()
//            .rotation(180f)
//            .setDuration(200)
//            .start()
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_spin_issue, parent, false)
        getItem(position)?.let { item ->
            setItem(view, item)
        }
        return view
    }

    private fun setItem(view: View, item : IssueCategory) {
        val tvIssueTitle = view.findViewById<TextView>(R.id.tv_issue_title)
        val ivIssue = view.findViewById<ImageView>(R.id.iv_issue)
        tvIssueTitle.text = item.title
    }
}