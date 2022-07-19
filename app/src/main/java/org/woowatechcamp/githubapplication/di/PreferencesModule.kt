package org.woowatechcamp.githubapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.woowatechcamp.githubapplication.util.AuthPreferences

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun providesSharedPreferences(
        @ApplicationContext context: Context
    ) = AuthPreferences(context)
}