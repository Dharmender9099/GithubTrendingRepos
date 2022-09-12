package com.example.trendingrepos.datasource.network

object TrendingReposResponse {
    val id = "101"
    val name = "Alice"
    val fullName = "square/okhttp"
    val description = "Square’s meticulous HTTP client for the JVM, Android, and GraalVM."
    val url = "https://google.com"

    val totalCount = 10
    val nextPage = 2
    val items = "{\n" +
            "        \"created_at\": \"2012-07-23T13:42:55Z\",\n" +
            "        \"description\": \"$description\",\n" +
            "        \"forks_count\": 8932,\n" +
            "        \"full_name\": \"$fullName\",\n" +
            "        \"id\": $id,\n" +
            "        \"isSelected\": false,\n" +
            "        \"language\": \"Kotlin\",\n" +
            "        \"name\": \"$name\",\n" +
            "        \"open_issues\": 144,\n" +
            "        \"owner\": {\n" +
            "            \"avatar_url\": \"https://avatars.githubusercontent.com/u/82592?v\\u003d4\",\n" +
            "            \"id\": 82592,\n" +
            "            \"login\": \"square\"\n" +
            "        },\n" +
            "        \"stargazers_count\": 42802,\n" +
            "        \"updated_at\": \"2022-09-10T09:25:28Z\",\n" +
            "        \"html_url\": \"$url\",\n" +
            "        \"watchers\": 42802\n" +
            "    }"

    //Success
    val success =
        "{ \"total_count\": $totalCount, \"items\": ${arrayListOf(items, items)}, \"nextPage\": $nextPage }"

    //Failure
     val failure = "{ \"data\":  \"Error\", \"message\": \"Failed to load data\" }"
}