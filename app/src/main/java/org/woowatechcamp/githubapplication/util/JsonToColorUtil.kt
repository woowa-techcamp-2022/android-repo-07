package org.woowatechcamp.githubapplication.util

import android.app.Application
import android.content.Context
import android.graphics.Color
import org.json.JSONObject
import javax.inject.Inject

class JsonToColorUtil @Inject constructor(
    private val applicationContext: Application,
) {
    fun parseJson(language: String?): Int? {
        language?.let { lang ->
            val jsonFileString = getJsonDataFromAsset(applicationContext)

            jsonFileString?.let {
                return Color.parseColor(
                    JSONObject(it).getJSONObject(lang).getString("color")
                )
            }
        }
        return null
    }

    private fun getJsonDataFromAsset(context: Context): String? {
        runCatching { context.assets.open("colors.json").bufferedReader().use { it.readText() } }
            .onSuccess {
                return it
            }
            .onFailure {
                return it.message
            }
        return null
    }
}
