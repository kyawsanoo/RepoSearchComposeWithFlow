package kso.repo.search.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kso.repo.search.app.CurrentNetworkStatus
import kso.repo.search.dataSource.api.RestDataSource
import kso.repo.search.dataSource.db.RepoSearchDatabase
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.model.networkBoundResource
import javax.inject.Inject

interface RepoSearchBaseRepository{

    fun getRepoListNetworkBoundResource(s: String): Flow<Resource<List<Repo>>>

}

class RepoSearchRepository @Inject constructor(
    private val apiDataSource: RestDataSource,
    private val dbDataSource: RepoSearchDatabase,
    private val appContext: Context
    ): RepoSearchBaseRepository {

    private val repoDetailDao = dbDataSource.repoDetailDao()

    override fun getRepoListNetworkBoundResource(s: String): Flow<Resource<List<Repo>>> {

        return networkBoundResource(

            // make request
            fetchRemote = {
                Log.e("Repository", "fetchRemote()")
                apiDataSource.searchRepos(s, 50)
            },

            // extract data
            getDataFromResponse = {
                Log.e("Repository", "getDataFromResponse()")
                it.body()!!.items
            },

            // save data
            saveFetchResult = {
                    repos ->
                Log.e("Repository", "saveFetchResult()")
                repoDetailDao.insertToRepoDetail(repos)

            },

            // return saved data
            fetchLocal = {
                Log.e("Repository", "fetchLocal()")
                repoDetailDao.getRepoDetail()
            },

            // should fetch data from remote api or local db
            shouldFetch = {
                Log.e("Repository", "shouldFetch()")
                CurrentNetworkStatus.getNetwork(appContext)
            }


        ).flowOn(Dispatchers.IO)
    }



}