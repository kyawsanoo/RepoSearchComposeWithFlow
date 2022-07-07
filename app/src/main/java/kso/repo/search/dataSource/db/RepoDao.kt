package kso.repo.search.dataSource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kso.repo.search.model.Repo

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Repo>)

    @Query("DELETE FROM Repos")
    suspend fun deleteAll()

    @Query("SELECT * FROM Repos WHERE name IN (:userNames)")
    fun getRepos(userNames: String): Flow<List<Repo>>
}