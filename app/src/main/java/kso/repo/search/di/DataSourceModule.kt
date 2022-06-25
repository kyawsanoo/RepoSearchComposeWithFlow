package kso.repo.search.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kso.repo.search.dataSource.RestDataSource
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
    fun provideRestDataSource(retrofit: Retrofit):RestDataSource{
        return retrofit.create(RestDataSource::class.java)
    }


}