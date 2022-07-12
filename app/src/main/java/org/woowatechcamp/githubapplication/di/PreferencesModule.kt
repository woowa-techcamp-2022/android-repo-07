package org.woowatechcamp.githubapplication.di

import android.app.Application
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
    @Singleton
    fun providesSharedPreferences(
        @ApplicationContext context : Application
    ) = AuthPreferences(context)
}