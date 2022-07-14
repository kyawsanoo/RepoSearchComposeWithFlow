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

    @Query("DELETE FROM Repo")
    suspend fun deleteAll()

    @Query("SELECT * FROM Repo WHERE name IN (:repoNames)")
    fun getRepos(repoNames: String): Flow<List<Repo>>

    @Query("SELECT * FROM Repo WHERE name LIKE '%' || (:repoName) || '%'")
    fun getFilteredRepos(repoName: String?): Flow<List<Repo>>

    @Query("DELETE FROM Repo")
    abstract fun deleteAllRepos()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertRepo(vararg repo: Repo)
}