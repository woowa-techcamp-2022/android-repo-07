package org.woowatechcamp.githubapplication

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

@HiltAndroidApp
class GithubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application : GithubApplication
    }
}
