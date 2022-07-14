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
import org.woowatechcamp.githubapplication.GithubApplication
import org.woowatechcamp.githubapplication.data.auth.AuthService
import org.woowatechcamp.githubapplication.data.issue.IssueService
import org.woowatechcamp.githubapplication.data.noti.NotiService
import org.woowatechcamp.githubapplication.data.user.UserService
import org.woowatechcamp.githubapplication.network.TokenInterceptor
import org.woowatechcamp.githubapplication.util.AuthPreferences
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
        preferences : AuthPreferences
    ) : Interceptor = TokenInterceptor(preferences)

    @Provides
    fun providesOkHttpClient(
        @TokenInter tokenInterceptor : Interceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(tokenInterceptor)
            .build()
    }

    // 로그인 시에만 사용하는 거라, 이후 지울 수 있는 방식으로 변경하기
    // 로그인 시에 사용하는 Retrofit
    @AuthRetrofit
    @Provides
    fun providesAuthRetrofit(
       okHttpClient: OkHttpClient
    ) : Retrofit {
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_BASE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    // API 연동을 위한 Retrofit
    @GithubRetrofit
    @Provides
    fun providesGithubRetrofit(
        okHttpClient: OkHttpClient
    ) : Retrofit {
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_API)
            .baseUrl(BuildConfig.GITHUB_BASE)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesAuthService(
        @AuthRetrofit retrofit : Retrofit) : AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    fun providesIssueService(
        @GithubRetrofit retrofit : Retrofit
    ) : IssueService = retrofit.create(IssueService::class.java)

    @Provides
    fun providesUserService(
        @GithubRetrofit retrofit : Retrofit
    ) : UserService = retrofit.create(UserService::class.java)

    @Provides
    fun providesNotiService(
        @GithubRetrofit retrofit: Retrofit
    ) : NotiService = retrofit.create(NotiService::class.java)

}