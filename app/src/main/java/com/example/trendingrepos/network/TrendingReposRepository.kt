package com.example.trendingrepos.network

import com.example.trendingrepos.data.TrendingReposResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrendingReposRepository @Inject constructor(private val trendingReposApi: TrendingReposApi) {

    /** Request to get github trending repos
     * @param query: Key for get trending repos of specific technology like java,kotlin,web
     * @return object of [TrendingReposResponse]
     */
    suspend fun getTrendingRepos(query: String) =
        trendingReposApi.getTrendingRepos(query, PAGE_COUNT, ITEM_PER_PAGE)

    companion object {
        private const val ITEM_PER_PAGE = 50
        private const val PAGE_COUNT = 1
    }
}