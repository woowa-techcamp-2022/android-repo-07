package org.woowatechcamp.githubapplication.util.ext

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.math.max

fun Bitmap.getRoundDrawable(res: Resources): RoundedBitmapDrawable {
    val round = RoundedBitmapDrawableFactory.create(res, this)
    round.cornerRadius = max(width, height) / 2f
    return round
}

suspend fun String.setBitmapWithCoil(
    context: Context,
    completed: (Bitmap) -> Unit
) {
    val loading = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(this)
        .build()
    val result = (loading.execute(request) as SuccessResult).drawable
    val bitmap = (result as BitmapDrawable).bitmap
    withContext(Dispatchers.Main) {
        completed(bitmap)
    }
}

fun String.setBitmap(
    completed: (Bitmap) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val inputStream = URL(this@setBitmap).openStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        withContext(Dispatchers.Main) {
            completed(bitmap)
        }
    }
}