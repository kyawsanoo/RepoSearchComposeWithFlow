package kso.repo.search.repository

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kso.repo.search.app.CurrentNetworkStatus
import kso.repo.search.dataSource.api.RestDataSource
import kso.repo.search.dataSource.db.RepoSearchDatabase
import kso.repo.search.model.Keyword
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.model.networkBoundResource
import javax.inject.Inject

interface AppRepository{

    fun getRepoListNetworkBoundResource(s: String): Flow<Resource<List<Repo>>>
    fun getKeywordListNetworkBoundResource(s: String): Flow<Resource<List<Keyword>>>

}

class RepoSearchAppRepository @Inject constructor(
    private val apiDataSource: RestDataSource,
    private val dbDataSource: RepoSearchDatabase,
    private val appContext: Context
    ): AppRepository {

    private val keywordDao = dbDataSource.keywordDao()
    private val repoDao = dbDataSource.repoDao()

    override fun getRepoListNetworkBoundResource(s: String): Flow<Resource<List<Repo>>> {

        return networkBoundResource(

            query = {
                Log.e("Repository", "in query()")
                repoDao.getFilteredRepos(s)
            },

            fetch = {
                Log.e("Repository", "in fetch()")
                apiDataSource.searchRepos(s).items
            },

            filterFetch = {
                    cachedRepos,  apiRepos ->
                Log.e("Repository", "in filterFetch")
                when(apiRepos.isNotEmpty() && cachedRepos.isNotEmpty()){
                    true -> apiRepos.flatMap {
                            repos -> cachedRepos.filter {
                        repos.id != it.id
                    }.toList()
                        apiRepos
                    }
                    else -> {
                    }
                }
                when(apiRepos.isEmpty()){
                    true -> {
                        Log.e("Repository", "in filterFetch apiRepos Empty ")
                        cachedRepos
                    }
                    else -> {
                        Log.e("Repository", "in filterFetch apiRepos Not Empty")
                        apiRepos
                    }
                }

            },

            saveFetchResult = {
                    repos ->
                Log.e("Repository", "in saveFetchResult()")
                dbDataSource.withTransaction {
                    repoDao.insertAll(repos)
                }
            },


            shouldFetch = {
                Log.e("Repository", "in shouldFetch()")
                CurrentNetworkStatus.getNetwork(appContext)
            }


        )
    }


    override fun getKeywordListNetworkBoundResource(s: String): Flow<Resource<List<Keyword>>>
            = networkBoundResource(

        query = {
            when(s.isEmpty()){
                true  ->
                    keywordDao.getAllKeywords()
                else -> {
                    keywordDao.getMatchedKeywords(s)
                }
            }

        },

        fetch = {
            Log.e("Repository", "in fetch(): Keywords")
            val apiKeywords = apiDataSource.getAllKeywordList()
            Log.e("Repository", "all keyword size ${apiKeywords.size}")

            apiKeywords

        },

        filterFetch = {
                cachedKeywords,  apiKeywords ->
            Log.e("Repository", "in filterFetch()")
            apiKeywords.flatMap {
                    repos -> cachedKeywords.filter {
                repos.id != it.id
            }.toList()
                apiKeywords
            }
        },

        saveFetchResult = {
                keywords ->
            Log.e("Repository", "in saveFetchResult()")
            dbDataSource.withTransaction {
                keywordDao.insertAll(keywords)
            }
        },


        shouldFetch = {

            Log.e("Repository", "in shouldFetch()")
            CurrentNetworkStatus.getNetwork(appContext)
        }

    )
}
