package kso.repo.search.dataSource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kso.repo.search.model.Keyword

@Dao
interface KeywordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Keyword>)

    @Query("DELETE FROM Keywords")
    suspend fun deleteAll()

    @Query("SELECT * FROM Keywords WHERE name IN (:names)")
    fun getMatchedKeywords(names: String): Flow<List<Keyword>>

    @Query("SELECT * FROM Keywords")
    fun getAllKeywords(): Flow<List<Keyword>>
}