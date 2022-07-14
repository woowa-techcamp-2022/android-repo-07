package org.woowatechcamp.githubapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GithubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application : GithubApplication
        const val AUTH = "https://github.com/login/oauth/authorize"
        const val ACCESS = "https://github.com/login/oauth/access_token"
        const val BASE = "https://github.com/"
    }
}
