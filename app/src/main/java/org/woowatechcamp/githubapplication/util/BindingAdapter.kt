package org.woowatechcamp.githubapplication.util

import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("app:underlineText")
fun TextView.setUnderlineText(text: String?) {
    paintFlags = Paint.UNDERLINE_TEXT_FLAG
    this.text = text
}

@BindingAdapter("app:imageUrl")
fun ImageView.setImage(imageUrl: String?) {
    imageUrl?.let {
        this.load(imageUrl)
    }
}
