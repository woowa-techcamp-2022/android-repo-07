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
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        Pref  = EncryptedSharedPreferences.create(
        "important_data",
        masterKeyAlias,
        this,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        lateinit var application : GithubApplication
        const val AUTH = "https://github.com/login/oauth/authorize"
        const val ACCESS = "https://github.com/login/oauth/access_token"
        const val BASE = "https://github.com/"
        const val ACCESS_TOKEN = "access_token"
        const val CODE = "code"
        lateinit var Pref : SharedPreferences
    }
}

