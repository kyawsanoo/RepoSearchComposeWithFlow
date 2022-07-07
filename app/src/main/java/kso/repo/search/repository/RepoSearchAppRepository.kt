package kso.repo.search.repository

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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
    private val dbDataSource: RepoSearchDatabase
    ): AppRepository {

    private val keywordDao = dbDataSource.keywordDao()
    private val repoDao = dbDataSource.repoDao()

    @OptIn(FlowPreview::class)
    override fun getRepoListNetworkBoundResource(s: String): Flow<Resource<List<Repo>>>
    = networkBoundResource(
        query = {
            repoDao.getRepos(s)
        },
        fetch = {
            Log.e("Repository", "in fetch(): Repos")
            val apiRepos = apiDataSource.searchRepos(s).items
            Log.e("Repository", "all apiRepos size ${apiRepos.size}")

            apiRepos

        },
        filterFetch = {
                cachedRepos,  apiRepos ->
            apiRepos.flatMap {
                    repos -> cachedRepos.filter {
                repos.id != it.id
            }.toList()
            apiRepos
          }
        },

        saveFetchResult = {
                repos ->
                    dbDataSource.withTransaction {
                        repoDao.insertAll(repos)
                    }
        },

        shouldFetch = {
            repos ->
                //repos.none { repo -> repo.name.compareTo(s, false) ==0 }
                repos.isEmpty()
        }
    )


    @OptIn(FlowPreview::class)
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
            apiKeywords.flatMap {
                    repos -> cachedKeywords.filter {
                repos.id != it.id
            }.toList()
                apiKeywords
            }
        },

        saveFetchResult = {
                keywords ->
            dbDataSource.withTransaction {
                keywordDao.insertAll(keywords)
            }
        },

        shouldFetch = {
                keywords -> keywords.isEmpty()
        }
    )
}