package com.example.trendingrepos.network

import com.example.trendingrepos.data.TrendingReposResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingReposApi {
    @GET("search/repositories?sort=stars")
    suspend fun getTrendingRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): TrendingReposResponse

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}