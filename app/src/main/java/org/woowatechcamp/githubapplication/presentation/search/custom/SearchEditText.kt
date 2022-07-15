package org.woowatechcamp.githubapplication.presentation.search.custom

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import org.woowatechcamp.githubapplication.R
import org.woowatechcamp.githubapplication.databinding.LayoutSearchEditBinding

class SearchEditText(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private var keyListener: (() -> Unit)? = null
    private var deleteClickListener: (() -> Unit)? = null
    private var textChangeListener: ((Boolean) -> Unit)? = null
    val text: Editable
        get() = layoutAuthEditTextBinding.etSearch.text
    private lateinit var layoutAuthEditTextBinding: LayoutSearchEditBinding

    // 삭제 관련 서버통신
    fun setDeleteClickListener(listener: () -> Unit) {
        deleteClickListener = listener
    }

    // 엔터 관련 서버통신
    fun setKeyListener(listener: () -> Unit) {
        keyListener = listener
    }

    // textChange 관련 서버통신
    fun setTextChangeListener(listener: (Boolean) -> Unit) {
        textChangeListener = listener
    }

    init {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(initView())
        initClickEvent()
        initTextChaneEvent()
    }

    private fun initView(): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        layoutAuthEditTextBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_search_edit, this, false)

        return layoutAuthEditTextBinding.root
    }

    private fun initClickEvent() {
        with(layoutAuthEditTextBinding) {
            ibDelete.setOnClickListener {
                clear()
                deleteClickListener?.invoke()
            }
        }
    }

    private fun clear() {
        layoutAuthEditTextBinding.etSearch.text.clear()
    }

    private fun initTextChaneEvent() {
        with(layoutAuthEditTextBinding) {
            etSearch.addTextChangedListener {
                with(etSearch.text.isNotEmpty()) {
                    isNotEmpty = this
                    layoutEdit.isSelected = this
                    textChangeListener?.invoke(this)
                }
            }
        }
    }
}
