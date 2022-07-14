package kso.repo.search.dataSource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kso.repo.search.model.Keyword
import kso.repo.search.model.Repo
import kso.repo.search.model.Owner

//@TypeConverters(Converters::class)
@Database(entities = [Repo::class, Owner::class, Keyword::class], version = 1, exportSchema = false)
abstract class RepoSearchDatabase() : RoomDatabase() {

    abstract fun repoDao(): RepoDao

    abstract fun ownerDao(): OwnerDao

    abstract fun repoDetailDao() : RepoDetailDao

    abstract fun keywordDao(): KeywordDao


}