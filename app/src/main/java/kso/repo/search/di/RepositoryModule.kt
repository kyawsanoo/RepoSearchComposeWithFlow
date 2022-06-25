package kso.repo.search.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kso.repo.search.repository.RepoSearchAppRepository
import kso.repo.search.repository.AppRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun repoSearchAppRepository(repo:RepoSearchAppRepository) : AppRepository

}