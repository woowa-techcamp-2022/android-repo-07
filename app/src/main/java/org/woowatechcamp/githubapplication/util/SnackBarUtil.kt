package org.woowatechcamp.githubapplication.util

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import org.woowatechcamp.githubapplication.R


fun showSnackBar(view: View, message: String, context: Context) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setTextColor(
            ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.white)
            )
        )
        .setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.navy)
            )
        )
        .show()
}