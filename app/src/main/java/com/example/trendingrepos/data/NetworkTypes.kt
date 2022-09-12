package com.example.trendingrepos.data

import com.google.gson.annotations.SerializedName

/**
 * Serialize trending github repos result in [TrendingReposResponse] using Gson
 */
data class TrendingReposResponse(
    @SerializedName("total_count")
    val total: Int = 0,
    @SerializedName("items")
    val items: List<TrendingRepo> = emptyList(),
    val nextPage: Int? = null
)

/***
 * Used for getting details of each trending item
 */
data class TrendingRepo(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("html_url")
    val url: String,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("forks_count")
    val forks: Int,
    @SerializedName("language")
    val language: String?,
    @SerializedName("watchers")
    val watchers: Int,
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("created_at")
    val createDate: String,
    @SerializedName("updated_at")
    val updateDate: String,
    @SerializedName("open_issues")
    val openIssues: Int,
    var isSelected: Boolean
) : Comparable<Any?> {
    override operator fun compareTo(other: Any?): Int {
        val trendingList = other as TrendingRepo
        return if (trendingList.isSelected == isSelected) {
            0
        } else 1
    }
}

data class Owner(
    val id: Int,
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)