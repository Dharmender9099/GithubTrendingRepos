package com.example.trendingrepos.network

import com.example.trendingrepos.data.TrendingReposResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrendingReposRepository @Inject constructor(private val trendingReposApi: TrendingReposApi) {

    /** Request for get github trending repos
     * @param query: key for get trending repos of specific technology like java,kotlin,web
     * @return object of [TrendingReposResponse]
     */
    suspend fun getTrendingRepos(query: String) = trendingReposApi.getTrendingRepos(query, 1, 50)
}