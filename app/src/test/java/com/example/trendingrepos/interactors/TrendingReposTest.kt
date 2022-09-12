package com.example.trendingrepos.interactors

import com.google.gson.GsonBuilder
import com.example.trendingrepos.datasource.network.TrendingReposResponse
import com.example.trendingrepos.network.TrendingReposApi
import com.example.trendingrepos.network.TrendingReposRepository
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

/**
 * 1. TrendingRepos Success
 * 2. TrendingRepos Failure (Invalid data)
 */
class TrendingReposTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var trendingReposRepository: TrendingReposRepository

    // dependencies
    private lateinit var service: TrendingReposApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("https://api.github.com/")
        service = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(TrendingReposApi::class.java)
        trendingReposRepository = TrendingReposRepository(service)
        // instantiate the system in test
        runBlocking {
            trendingReposRepository.getTrendingRepos("language:Kotlin")
        }
    }

    @Test
    fun trendingReposSuccess() = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TrendingReposResponse.success)
        )
        // Trending repos details
        val itemList = TrendingReposResponse.items
    }


    @Test
    fun trendingReposFailure() = runBlocking {
        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(TrendingReposResponse.failure)
        )
        val failure = TrendingReposResponse.failure
    }
}