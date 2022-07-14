package kso.repo.search.dataSource.db

import android.util.Log
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kso.repo.search.model.Repo
import kso.repo.search.model.RepoDetail

@Dao
abstract class RepoDetailDao: RepoDao, OwnerDao {

    fun insertToRepoDetail(repos: List<Repo>) {
        // delete previous data
        deleteAllRepos()
        deleteAllOwners()

        // save new data
        for (r in repos) {
            r.owner.repoId = r.id
            upsertOwner(r.owner)
        }
        insertReposToRepoDetail(repos)
    }

    fun getRepoDetail() : Flow<List<Repo>> {
        val repoDetail = _getAllFromRepoDetail()
        val repos: MutableList<Repo> = mutableListOf()
        for (i in repoDetail) {
            i.repo.owner = i.owner
            repos.add(i.repo)
        }
        Log.e("RepoDetailDao", "getRepos from RepoDetail: size ${repos.size}")
        return flow { emit(repos) }
    }

    fun getRepoDetailById(repoId: Long) : Flow<Repo> {
        val repoWithOwner = _getRepoDetailById(repoId)
        val repo = repoWithOwner.repo
        repo.owner = repoWithOwner.owner
        return flow { emit(repo) }
    }

    // insert or update if exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsertRepos(repos: List<Repo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertReposToRepoDetail(repos: List<Repo>)

    @Transaction
    @Query("SELECT * FROM Repo, Owner WHERE Repo.id = Owner.repoId ORDER BY Repo.stargazersCount DESC")
    abstract fun _getAllFromRepoDetail() : List<RepoDetail>

    @Transaction
    @Query("SELECT * FROM Repo INNER JOIN Owner ON Owner.repoId = Repo.id WHERE Repo.id = :repoId")
    abstract fun _getRepoDetailById(repoId: Long) : RepoDetail



}