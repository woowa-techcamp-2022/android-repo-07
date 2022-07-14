package org.woowatechcamp.githubapplication.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.woowatechcamp.githubapplication.util.AuthPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun providesSharedPreferences(
        @ApplicationContext context : Context
    ) = AuthPreferences(context)
}