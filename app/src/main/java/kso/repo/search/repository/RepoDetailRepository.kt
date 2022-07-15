package kso.repo.search.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kso.repo.search.app.CurrentNetworkStatus
import kso.repo.search.dataSource.api.RestDataSource
import kso.repo.search.dataSource.db.RepoSearchDatabase
import kso.repo.search.model.*
import kso.repo.search.networkboundresource.repoDetailNetworkBoundResource
import javax.inject.Inject

interface RepoDetailBaseRepository{

    fun getRepoDetailNetworkBoundResource(repoId: Long, ownerLogin: String, repoName: String): Flow<Resource<Repo>>

}

class RepoDetailRepository @Inject constructor(
    private val apiDataSource: RestDataSource,
    private val dbDataSource: RepoSearchDatabase,
    private val appContext: Context
    ): RepoDetailBaseRepository{

    private val repoDetailDao = dbDataSource.repoDetailDao()

    override fun getRepoDetailNetworkBoundResource(repoId: Long, ownerLogin: String, repoName: String): Flow<Resource<Repo>> {

        return repoDetailNetworkBoundResource(

            // make request
            fetchRemote = {
                Log.e("Repository", "fetchRemote()")
                apiDataSource.getRepo(owner = ownerLogin, name = repoName)
            },

            // extract data
            getDataFromResponse = {
                Log.e("Repository", "getDataFromResponse()")
                it.body()!!
            },

            // save data
            saveFetchResult = {
                    repo ->
                Log.e("Repository", "saveFetchResult()")
                repoDetailDao.upsertRepo(repo)
            },

            // return saved data
            fetchLocal = {
                Log.e("Repository", "fetchLocal()")
                repoDetailDao.getRepoDetailById(repoId)

            },

            // should fetch data from remote api or local db
            shouldFetch = {
                Log.e("Repository", "shouldFetch()")
                CurrentNetworkStatus.getNetwork(appContext)
            }


        ).flowOn(Dispatchers.IO)
    }



}
