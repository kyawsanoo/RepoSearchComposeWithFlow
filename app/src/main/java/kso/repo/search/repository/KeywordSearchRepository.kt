package kso.repo.search.repository

import kso.repo.search.model.*
import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kso.repo.search.app.CurrentNetworkStatus
import kso.repo.search.dataSource.api.RestDataSource
import kso.repo.search.dataSource.db.RepoSearchDatabase
import kso.repo.search.networkboundresource.keywordSearchNetworkBoundResource
import javax.inject.Inject

interface KeywordSearchBaseRepository{

    fun getKeywordListNetworkBoundResource(s: String): Flow<Resource<List<Keyword>>>

}

class KeywordSearchRepository @Inject constructor(
    private val apiDataSource: RestDataSource,
    private val dbDataSource: RepoSearchDatabase,
    private val appContext: Context
): KeywordSearchBaseRepository{

    private val keywordDao = dbDataSource.keywordDao()

    override fun getKeywordListNetworkBoundResource(s: String): Flow<Resource<List<Keyword>>>
            = keywordSearchNetworkBoundResource(

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

    ).flowOn(Dispatchers.IO)
}
