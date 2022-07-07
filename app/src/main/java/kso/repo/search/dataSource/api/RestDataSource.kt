package kso.repo.search.dataSource.api

import kso.repo.search.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestDataSource {

    @GET("/repositories")
    suspend fun getAllKeywordList(
    ): List<Keyword>


    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") q: String
    ): RepoSearchResponse



}