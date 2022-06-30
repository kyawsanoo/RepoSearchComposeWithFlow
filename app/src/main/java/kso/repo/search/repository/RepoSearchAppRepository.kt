package kso.repo.search.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kso.repo.search.dataSource.RestDataSource
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.model.User
import javax.inject.Inject

interface AppRepository{

    fun getUserList(): Flow<Resource<List<User>>>
    fun getUser(userName: String): Flow<Resource<User>>
    fun getRepoList(userName: String): Flow<Resource<List<Repo>>>
    fun getRepoDetail(userName: String, repoName: String): Flow<Resource<Repo>>
    suspend fun getSearchRepoList(s: String): Flow<Resource<List<Repo>>>

}

class RepoSearchAppRepository @Inject constructor(private val dataSource: RestDataSource): AppRepository {

    override fun getUserList(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)
        val resource = try {
            val response = dataSource.getUsers()
            Resource.Success(response)
        } catch (e: Throwable) {
            Resource.Fail(e.toString())
        }
        emit(resource)
    }

    override fun getUser(
        userName: String
    ): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        val resource = try {
            val response = dataSource.getUser(userName)
            Resource.Success(response)
        } catch (e: Throwable) {

            Resource.Fail(e.toString())
        }
        emit(resource)
    }

    override fun getRepoList(
        userName: String
    ): Flow<Resource<List<Repo>>> = flow {
        emit(Resource.Loading)
        val resource = try {
            val response = dataSource.getRepoList(userName)
            Resource.Success(response)
        } catch (e: Throwable) {
            Resource.Fail(e.toString())
        }
        emit(resource)
    }

    override fun getRepoDetail(
        userName: String,
        repoName: String
    ): Flow<Resource<Repo>> = flow {
        emit(Resource.Loading)
        val resource = try {
            val response = dataSource.getRepoDetails(userName, repoName)
            Resource.Success(response)
        } catch (e: Throwable) {
            Resource.Fail(e.toString())
        }
        emit(resource)
    }

    override suspend fun getSearchRepoList(q: String): Flow<Resource<List<Repo>>> = flow {
        emit(Resource.Loading)
        val resource = try {
            val response = dataSource.searchRepos(q)
            Log.e("Repository", "Search Repo Size: ${response.items.size}")
            Resource.Success(response.items)
        } catch (e: Throwable) {
            Resource.Fail(e.toString())
        }
        emit(resource)
    }


}