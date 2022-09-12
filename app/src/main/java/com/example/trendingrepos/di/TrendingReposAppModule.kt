package com.example.trendingrepos.di

import com.example.trendingrepos.BuildConfig
import com.example.trendingrepos.network.TrendingReposApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrendingReposAppModule {
    private var okHttpClient = OkHttpClient.Builder()

    /**
     * Add logging interceptor
     */
    init {
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(getLoggingInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
        }
    }

    /**
     * Create retrofit instance for calling REST apis
     */
    @Provides
    @Singleton
    fun retrofit(): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl(TrendingReposApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    /**
     * TrendingGithubRepos api client
     */
    @Provides
    @Singleton
    fun trendingRepos(retrofit: Retrofit): TrendingReposApi =
        retrofit.create(TrendingReposApi::class.java)

    /**
     * Okhttp logging interceptor only for debug build
     */
    private fun getLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
}