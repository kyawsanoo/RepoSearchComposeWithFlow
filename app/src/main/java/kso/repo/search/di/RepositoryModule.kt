package kso.repo.search.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kso.repo.search.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun repoSearchRepository(repo:RepoSearchRepository) : RepoSearchBaseRepository

    @Binds
    @Singleton
    abstract fun repoDetailRepository(repo: RepoDetailRepository) : RepoDetailBaseRepository

    @Binds
    @Singleton
    abstract fun keywordSearchRepository(repo:KeywordSearchRepository) : KeywordSearchBaseRepository

}