package kso.repo.search.dataSource

import kso.repo.search.model.Repo
import kso.repo.search.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface RestDataSource {

    @GET("users?per_page=100")
    suspend fun getUsers(): List<User>

    @GET("users/{user_name}")
    suspend fun getUser(
        @Path("user_name") userName: String
    ): User

    @GET("users/{user_name}/repos")
    suspend fun getRepoList(
        @Path("user_name") userName: String?
    ): List<Repo>

    @GET("/repos/{user_name}/{repo_name}")
    suspend fun getRepoDetails(
        @Path("user_name") userName: String?,
        @Path("repo_name") repoName: String?
    ): Repo
}