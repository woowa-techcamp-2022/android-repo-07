package org.woowatechcamp.githubapplication.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject

class AuthPreferences @Inject constructor(
    context: Context
) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val preferences = EncryptedSharedPreferences.create(
        "important_data",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var accessToken: String
        set(value) = preferences.edit { putString(ACCESS_TOKEN, value) }
        get() = preferences.getString(ACCESS_TOKEN, "") ?: ""

    companion object {
        private const val ACCESS_TOKEN = "access_token"
    }

}