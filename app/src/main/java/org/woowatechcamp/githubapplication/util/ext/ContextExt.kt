package org.woowatechcamp.githubapplication.util.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view.findFocus(), 0)
}

fun Context.closeKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

inline fun <reified T : Activity> Context.buildIntent(
    vararg argument: Pair<String, Any?>
) = Intent(this, T::class.java).apply {
    putExtras(bundleOf(*argument))
}

inline fun <reified T : Activity> Context.startActivity(
    vararg argument: Pair<String, Any?>
) {
    startActivity(buildIntent<T>(*argument))
}

fun Context.startAction(
    intent: Pair<String, Uri>,
    vararg argument: Pair<String, Any?>
) {
    startActivity(
        Intent(intent.first, intent.second).apply {
            putExtras(bundleOf(*argument))
        }
    )
}

fun Context.startMail(
    vararg argument: Pair<String, Any?>
) {
    startActivity(
        Intent(ACTION_SEND).apply {
            type = "plain/text"
            putExtras(bundleOf(*argument))
        }
    )
}

fun Context.stringListFrom(id: Int): List<String> =
    resources.getStringArray(id).toList()


fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
