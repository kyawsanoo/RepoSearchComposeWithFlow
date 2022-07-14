package kso.repo.search.dataSource.api

import kso.repo.search.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestDataSource {

    @GET("/repositories")
    suspend fun getAllKeywordList(
    ): List<Keyword>


    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") q: String,
        @Query("per_page") perPage: Int
    ): Response<RepoSearchResponse>

    @GET("repos/{owner}/{name}")
    suspend fun getRepo(
        @Path("owner") owner: String,   // owner login
        @Path("name") name: String      // repo name
    ): Response<Repo>



}