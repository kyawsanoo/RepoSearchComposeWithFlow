package kso.repo.search.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kso.repo.search.dataSource.api.RestDataSource
import kso.repo.search.dataSource.db.RepoSearchDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    @Named("baseUrl")
    fun baseUrl() = "https://api.github.com"

    @Singleton
    @Provides
    fun provideRetrofit(@Named("baseUrl") baseUrl: String): Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()

    }

    @Singleton
    @Provides
    fun provideRestDataSource(retrofit: Retrofit): RestDataSource {
        return retrofit.create(RestDataSource::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabaseDataSource(app: Application) =
        Room.databaseBuilder(app, RepoSearchDatabase::class.java, "Repo_DB")
            .addMigrations()
            .build()


    @Singleton
    @Provides
    fun provideRepoDao(database: RepoSearchDatabase) =    database.repoDao()

    @Singleton
    @Provides
    fun provideOwnerDao(database: RepoSearchDatabase) =    database.ownerDao()

    @Singleton
    @Provides
    fun provideRepoDetailDao(database: RepoSearchDatabase) =    database.repoDetailDao()

    @Singleton
    @Provides
    fun provideKeywordDao(database: RepoSearchDatabase) =    database.keywordDao()
}