package kso.repo.search.dataSource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kso.repo.search.model.Keyword
import kso.repo.search.model.Repo
import kso.repo.search.model.User

@TypeConverters(Converters::class)
@Database(entities = [Repo::class, Keyword::class], version = 1, exportSchema = false)
abstract class RepoSearchDatabase() : RoomDatabase() {

    abstract fun repoDao(): RepoDao

    abstract fun keywordDao(): KeywordDao

}