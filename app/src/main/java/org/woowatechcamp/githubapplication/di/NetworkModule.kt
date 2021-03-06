package org.woowatechcamp.githubapplication.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.woowatechcamp.githubapplication.BuildConfig
import org.woowatechcamp.githubapplication.domain.repository.AuthRepository
import org.woowatechcamp.githubapplication.data.auth.AuthRepositoryImpl
import org.woowatechcamp.githubapplication.data.auth.AuthService
import org.woowatechcamp.githubapplication.domain.repository.IssueRepository
import org.woowatechcamp.githubapplication.data.issue.IssueRepositoryImpl
import org.woowatechcamp.githubapplication.data.issue.IssueService
import org.woowatechcamp.githubapplication.domain.usecase.IssueUseCase
import org.woowatechcamp.githubapplication.domain.repository.NotiRepository
import org.woowatechcamp.githubapplication.data.noti.NotiRepositoryImpl
import org.woowatechcamp.githubapplication.data.noti.NotiService
import org.woowatechcamp.githubapplication.domain.usecase.NotiUseCase
import org.woowatechcamp.githubapplication.data.search.SearchService
import org.woowatechcamp.githubapplication.domain.repository.UserRepository
import org.woowatechcamp.githubapplication.data.user.UserRepositoryImpl
import org.woowatechcamp.githubapplication.data.user.UserService
import org.woowatechcamp.githubapplication.network.TokenInterceptor
import org.woowatechcamp.githubapplication.domain.usecase.IssueUseCaseImpl
import org.woowatechcamp.githubapplication.data.noti.paging.NotiUseCaseImpl
import org.woowatechcamp.githubapplication.data.local.AuthPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GithubRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TokenInter

    @TokenInter
    @Provides
    fun providesTokenInterceptor(
        preferences: AuthPreferences
    ): Interceptor = TokenInterceptor(preferences)

    @Provides
    fun providesOkHttpClient(
        @TokenInter tokenInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(tokenInterceptor)
            .build()
    }

    // ????????? ????????? ???????????? ??????, ?????? ?????? ??? ?????? ???????????? ????????????
    // ????????? ?????? ???????????? Retrofit
    @AuthRetrofit
    @Provides
    fun providesAuthRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_BASE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // API ????????? ?????? Retrofit
    @GithubRetrofit
    @Provides
    fun providesGithubRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun providesAuthService(
        @AuthRetrofit retrofit: Retrofit
    ): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun providesIssueService(
        @GithubRetrofit retrofit: Retrofit
    ): IssueService = retrofit.create(IssueService::class.java)

    @Singleton
    @Provides
    fun providesUserService(
        @GithubRetrofit retrofit: Retrofit
    ): UserService = retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun providesNotiService(
        @GithubRetrofit retrofit: Retrofit
    ): NotiService = retrofit.create(NotiService::class.java)

    @Singleton
    @Provides
    fun providesSearchService(
        @GithubRetrofit retrofit: Retrofit
    ): SearchService = retrofit.create(SearchService::class.java)

    @Singleton
    @Provides
    fun providesIssueRepository(
        service: IssueService
    ): IssueRepository = IssueRepositoryImpl(service)

    @Singleton
    @Provides
    fun providesUserRepository(
        service: UserService
    ): UserRepository = UserRepositoryImpl(service)

    @Singleton
    @Provides
    fun providesNotiRepository(
        service: NotiService
    ): NotiRepository = NotiRepositoryImpl(service)

    @Singleton
    @Provides
    fun providesAuthRepository(
        service: AuthService,
        preferences: AuthPreferences
    ): AuthRepository = AuthRepositoryImpl(service, preferences)

    @Singleton
    @Provides
    fun providesIssueUseCase(
        service: IssueService
    ): IssueUseCase = IssueUseCaseImpl(service)

    @Singleton
    @Provides
    fun providesNotiUseCase(
        service: NotiService
    ): NotiUseCase = NotiUseCaseImpl(service)
}